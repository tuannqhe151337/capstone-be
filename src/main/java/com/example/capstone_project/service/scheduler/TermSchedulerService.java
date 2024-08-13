package com.example.capstone_project.service.scheduler;

import com.example.capstone_project.entity.*;
import com.example.capstone_project.entity.Currency;
import com.example.capstone_project.repository.*;
import com.example.capstone_project.repository.result.CostStatisticalByCurrencyResult;
import com.example.capstone_project.repository.result.PaginateExchange;
import com.example.capstone_project.service.result.CostResult;
import com.example.capstone_project.service.result.TotalCostByCurrencyResult;
import com.example.capstone_project.utils.enums.ExpenseStatusCode;
import com.example.capstone_project.utils.enums.ReportStatusCode;
import com.example.capstone_project.utils.enums.TermCode;
import lombok.RequiredArgsConstructor;

import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Component
@RequiredArgsConstructor
public class TermSchedulerService {
    private final TermRepository termRepository;
    private final ReportStatusRepository reportStatusRepository;
    private final FinancialReportRepository financialReportRepository;
    private final TermStatusRepository termStatusRepository;
    private final CurrencyRepository currencyRepository;
    private final CurrencyExchangeRateRepository currencyExchangeRateRepository;
    private final DepartmentRepository departmentRepository;
    private final CostTypeRepository costTypeRepository;
    private final ReportStatisticalRepository reportStatisticalRepository;
    private final FinancialPlanRepository planRepository;
    private final FinancialPlanExpenseRepository planExpenseRepository;
    private final ExpenseStatusRepository expenseStatusRepository;

    @Scheduled(cron = "0 00 00 * * *") // Execute at 12:00 AM every day
    @Transactional
    @Async
    public void startTerm() throws Exception {
        //START TERM
        List<Term> terms = termRepository.getListTermNeedToStart(TermCode.NEW, LocalDateTime.now());
        //change status to 2 (IN_PROGRESS)
        if (terms != null) {
            for (Term term : terms) {
                TermStatus inProgressStatus = termStatusRepository.findByCode(TermCode.IN_PROGRESS);
                term.setStatus(inProgressStatus);
                termRepository.save(term);

                ReportStatus newStatus = reportStatusRepository.findByCode(ReportStatusCode.NEW);

                // Create new report
                FinancialReport report = FinancialReport.builder()
                        .name(term.getName() + "_" + "Report")
                        .month(term.getFinalEndTermDate().toLocalDate())
                        .term(term)
                        .status(newStatus)
                        .build();

                // Generate report
                financialReportRepository.save(report);

            }
        }
    }

    @Scheduled(cron = "0 00 00 * * *") // Execute at 12:00 AM every day
    @Transactional
    @Async
    public void endTerm() throws Exception {
        //START TERM
        List<Term> terms = termRepository.getListTermNeedToClose(TermCode.IN_PROGRESS, LocalDateTime.now());
        //change status to CLOSED
        if (terms != null) {
            for (Term term : terms) {

                TermStatus closedStatus = termStatusRepository.findByCode(TermCode.CLOSED);
                term.setStatus(closedStatus);
                termRepository.save(term);

                List<FinancialPlanExpense> listExpenseNeedToClose = new ArrayList<>();

                ExpenseStatus denyStatus = expenseStatusRepository.findByCode(ExpenseStatusCode.DENIED);

                // Denied expenses not approval
                planRepository.findAllByTermId(term.getId()).forEach(plan -> {
                    // Get list expense have status waiting
                    planExpenseRepository.getListExpenseNeedToCloseByPlanId(plan.getId(), ExpenseStatusCode.NEW).forEach(expense -> {

                        // Change expense status from waiting to deny
                        expense.setStatus(denyStatus);
                        listExpenseNeedToClose.add(expense);
                    });

                    // Change expense status
                    planExpenseRepository.saveAll(listExpenseNeedToClose);

                });

                Currency defaultCurrency = currencyRepository.getDefaultCurrency();
                generateActualCostAndExpectedCostForPlan(term.getId(), defaultCurrency);
                generateActualCostAndExpectedCostForReport(term.getId(), defaultCurrency);
                generateReportStatistical(term.getId(), defaultCurrency);
            }
        }
    }

    private void generateActualCostAndExpectedCostForPlan(Long termId, Currency defaultCurrency) {
        List<FinancialPlan> plans = planRepository.getReferenceByTermId(termId);
        List<FinancialPlan> savePlan = new ArrayList<>();
        if (plans != null) {
            plans.forEach(plan -> {
                plan.setExpectedCost(calculateCost(planRepository.calculateCostByPlanId(plan.getId(), null), defaultCurrency).getCost());

                plan.setActualCost(calculateCost(planRepository.calculateCostByPlanId(plan.getId(), ExpenseStatusCode.APPROVED), defaultCurrency).getCost());
                savePlan.add(plan);
            });

            planRepository.saveAll(plans);
        }
    }


    private void generateActualCostAndExpectedCostForReport(Long termId, Currency defaultCurrency) {
        FinancialReport report = financialReportRepository.getReferenceByTermId(termId);
        if (report != null) {
            financialReportRepository.calculateCostByReportIdAndStatus(report.getId(), null);
            ;

            report.setExpectedCost(calculateCost(financialReportRepository.calculateCostByReportIdAndStatus(report.getId(), null), defaultCurrency).getCost());

            report.setActualCost(calculateCost(financialReportRepository.calculateCostByReportIdAndStatus(report.getId(), ExpenseStatusCode.APPROVED), defaultCurrency).getCost());
            financialReportRepository.save(report);
        }
    }

    private void generateReportStatistical(Long termId, Currency defaultCurrency) {
        FinancialReport report = financialReportRepository.getReferenceByTermId(termId);
        if (report != null) {

            List<CostStatisticalByCurrencyResult> costStaticalByCurrencies = reportStatisticalRepository.getListCostStatistical(report.getId(), ExpenseStatusCode.APPROVED);

            if (costStaticalByCurrencies != null) {
                List<ReportStatistical> reportStatistics = new ArrayList<>();

                // Inner hashmap: map by currency id
                HashMap<String, HashMap<Long, List<CostStatisticalByCurrencyResult>>> fromCurrencyIdHashMap = new HashMap<>();

                Set<PaginateExchange> monthYearSet = new HashSet<>();

                costStaticalByCurrencies.forEach(costStatisticalByCurrency -> {
                    fromCurrencyIdHashMap.putIfAbsent(costStatisticalByCurrency.getDepartmentId() + "_" + costStatisticalByCurrency.getCostTypeId(), new HashMap<>());
                    monthYearSet.add(PaginateExchange.builder()
                            .month(costStatisticalByCurrency.getMonth())
                            .year(costStatisticalByCurrency.getYear())
                            .build());
                });

                costStaticalByCurrencies.forEach(costStatisticalByCurrency -> {
                    fromCurrencyIdHashMap.get(costStatisticalByCurrency.getDepartmentId() + "_" + costStatisticalByCurrency.getCostTypeId())
                            .putIfAbsent(costStatisticalByCurrency.getCurrencyId(), new ArrayList<>());
                });

                costStaticalByCurrencies.forEach(costStatisticalByCurrency -> {
                    fromCurrencyIdHashMap.get(costStatisticalByCurrency.getDepartmentId() + "_" + costStatisticalByCurrency.getCostTypeId())
                            .get(costStatisticalByCurrency.getCurrencyId()).add(costStatisticalByCurrency);
                });

                // Get list exchange rates
                Set<Long> currencyIds = new HashSet<>();
                for (String depIdCostId : fromCurrencyIdHashMap.keySet()) {
                    currencyIds.addAll(fromCurrencyIdHashMap.get(depIdCostId).keySet());
                }

                currencyIds.add(defaultCurrency.getId());

                // Get list exchange rates
                List<CurrencyExchangeRate> exchangeRates = currencyExchangeRateRepository.getListCurrencyExchangeRateByMonthYear(monthYearSet.stream().toList(), currencyIds.stream().toList());

                // Outer hashmap: map by string (department id + cost type id), date, currency id
                HashMap<String, HashMap<String, HashMap<Long, BigDecimal>>> exchangeRateHashMap = new HashMap<>();

                costStaticalByCurrencies.forEach(costStatisticalByCurrencyResult -> {
                    exchangeRateHashMap.putIfAbsent(costStatisticalByCurrencyResult.getDepartmentId() + "_" + costStatisticalByCurrencyResult.getCostTypeId(), new HashMap<>());
                });

                exchangeRateHashMap.keySet().forEach(depIdCostId -> {
                    exchangeRates.forEach(exchangeRate -> {
                        exchangeRateHashMap.get(depIdCostId).putIfAbsent(exchangeRate.getMonth().format(DateTimeFormatter.ofPattern("M/yyyy")), new HashMap<>());
                    });
                });

                exchangeRateHashMap.keySet().forEach(depIdCostId -> {
                    exchangeRates.forEach(exchangeRate -> {
                        exchangeRateHashMap.get(depIdCostId).get(exchangeRate.getMonth().format(DateTimeFormatter.ofPattern("M/yyyy"))).put(exchangeRate.getCurrency().getId(), exchangeRate.getAmount());
                    });
                });

                for (String depIdCostId : fromCurrencyIdHashMap.keySet()) {
                    BigDecimal totalCost = BigDecimal.valueOf(0);
                    BigDecimal formAmount = null;
                    BigDecimal toAmount = null;
                    BigDecimal biggestCost = null;
                    BigDecimal maxBiggestCost = BigDecimal.valueOf(0);
                    HashMap<Long, List<CostStatisticalByCurrencyResult>> depIdCostIdHashMap = fromCurrencyIdHashMap.get(depIdCostId);

                    for (Long fromCurrencyId : depIdCostIdHashMap.keySet()) {

                        for (CostStatisticalByCurrencyResult costStatisticalByCurrency : depIdCostIdHashMap.get(fromCurrencyId)) {

                            formAmount = exchangeRateHashMap.get(costStatisticalByCurrency.getDepartmentId() + "_" + costStatisticalByCurrency.getCostTypeId())
                                    .get(costStatisticalByCurrency.getMonth() + "/" + costStatisticalByCurrency.getYear())
                                    .get(fromCurrencyId);

                            toAmount = exchangeRateHashMap.get(costStatisticalByCurrency.getDepartmentId() + "_" + costStatisticalByCurrency.getCostTypeId())
                                    .get(costStatisticalByCurrency.getMonth() + "/" + costStatisticalByCurrency.getYear())
                                    .get(defaultCurrency.getId());
                            System.out.println("BUG____REPORT");
                            System.out.println(costStatisticalByCurrency.getCost());
                            System.out.println(toAmount);
                            System.out.println(formAmount);
                            if (costStatisticalByCurrency.getCost() != null && toAmount != null && formAmount != null) {
                                totalCost = totalCost.add(costStatisticalByCurrency.getCost().multiply(toAmount).divide(formAmount, 2, RoundingMode.CEILING));
                            }

                            if (costStatisticalByCurrency.getBiggestCost() != null && toAmount != null && formAmount != null) {
                                biggestCost = costStatisticalByCurrency.getBiggestCost().multiply(toAmount).divide(formAmount, 2, RoundingMode.CEILING);
                            }

                            if (biggestCost != null) {
                                if (maxBiggestCost.longValue() < biggestCost.longValue()) {
                                    maxBiggestCost = costStatisticalByCurrency.getBiggestCost();
                                }
                            }
                        }
                    }
                    // Split the string using the "_" delimiter
                    String[] parts = depIdCostId.split("_");

                    // Parse the parts to Long
                    Long departmentId = Long.parseLong(parts[0]);
                    Long costTypeId = Long.parseLong(parts[1]);


                    reportStatistics.add(
                            ReportStatistical.builder()
                                    .report(report)
                                    .totalExpense(totalCost)
                                    .biggestExpenditure(maxBiggestCost)
                                    .department(departmentRepository.getReferenceById(departmentId))
                                    .costType(costTypeRepository.getReferenceById(costTypeId))
                                    .build());
                }
                reportStatisticalRepository.saveAll(reportStatistics);
            }
        }
    }

    private CostResult calculateCost(List<TotalCostByCurrencyResult> costByCurrencyResults, Currency defaultCurrency) {


        if (costByCurrencyResults == null) {
            return CostResult.builder().cost(BigDecimal.valueOf(0))
                    .currency(defaultCurrency)
                    .build();
        }

        // Inner hashmap: map by currency id
        HashMap<Long, List<TotalCostByCurrencyResult>> fromCurrencyIdHashMap = new HashMap<>();

        Set<PaginateExchange> monthYearSet = new HashSet<>();

        costByCurrencyResults.forEach(costByCurrency -> {
            fromCurrencyIdHashMap.putIfAbsent(costByCurrency.getCurrencyId(), new ArrayList<>());
        });

        costByCurrencyResults.forEach(costByCurrency -> {
            fromCurrencyIdHashMap.get(costByCurrency.getCurrencyId()).add(costByCurrency);
            monthYearSet.add(PaginateExchange.builder()
                    .month(costByCurrency.getMonth())
                    .year(costByCurrency.getYear())
                    .build());
        });

        // Get list exchange rates
        List<Long> currencyIds = new ArrayList<>(fromCurrencyIdHashMap.keySet().stream().toList());
        currencyIds.add(defaultCurrency.getId());

        // Get list exchange rates
        List<CurrencyExchangeRate> exchangeRates = currencyExchangeRateRepository.getListCurrencyExchangeRateByMonthYear(monthYearSet.stream().toList(), currencyIds);

        // Outer hashmap: map by date
        HashMap<String, HashMap<Long, BigDecimal>> exchangeRateHashMap = new HashMap<>();

        exchangeRates.forEach(exchangeRate -> {
            exchangeRateHashMap.putIfAbsent(exchangeRate.getMonth().format(DateTimeFormatter.ofPattern("M/yyyy")), new HashMap<>());
        });

        exchangeRates.forEach(exchangeRate -> {
            exchangeRateHashMap.get(exchangeRate.getMonth().format(DateTimeFormatter.ofPattern("M/yyyy"))).put(exchangeRate.getCurrency().getId(), exchangeRate.getAmount());
        });

        BigDecimal actualCost = BigDecimal.valueOf(0);

        for (Long fromCurrencyId : fromCurrencyIdHashMap.keySet()) {
            for (TotalCostByCurrencyResult costByCurrency : fromCurrencyIdHashMap.get(fromCurrencyId)) {
                BigDecimal formAmount = BigDecimal.valueOf(exchangeRateHashMap.get(costByCurrency.getMonth() + "/" + costByCurrency.getYear()).get(fromCurrencyId).longValue());
                BigDecimal toAmount = BigDecimal.valueOf(exchangeRateHashMap.get(costByCurrency.getMonth() + "/" + costByCurrency.getYear()).get(defaultCurrency.getId()).longValue());
                actualCost = actualCost.add(costByCurrency.getTotalCost().multiply(toAmount).divide(formAmount, 2, RoundingMode.CEILING));
            }
        }
        return CostResult.builder().cost(actualCost).currency(defaultCurrency).build();
    }

}