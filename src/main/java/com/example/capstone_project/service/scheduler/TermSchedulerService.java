package com.example.capstone_project.service.scheduler;

import com.example.capstone_project.entity.*;
import com.example.capstone_project.repository.*;
import com.example.capstone_project.service.TermService;
import com.example.capstone_project.utils.enums.ExpenseStatusCode;
import com.example.capstone_project.utils.enums.PlanStatusCode;
import com.example.capstone_project.utils.enums.TermCode;
import lombok.RequiredArgsConstructor;

import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class TermSchedulerService {
    private final TermService termService;
    private final TermRepository termRepository;
    private final FinancialPlanRepository planRepository;
    private final PlanStatusRepository planStatusRepository;
    private final FinancialPlanExpenseRepository planExpenseRepository;
    private final ExpenseStatusRepository expenseStatusRepository;
    private final FinancialReportExpenseRepository reportExpenseRepository;
    private final FinancialReportRepository reportRepository;


    @Scheduled(cron = "0 00 00 * * *") // Execute at 12:00 AM every day
    @Transactional
    @Async
    public void startTerm() throws Exception {
        //START TERM
        List<Term> terms = termRepository.getListTermNeedToStart(TermCode.NEW, LocalDateTime.now());
        //change status to 2 (IN_PROGRESS)
        if (terms != null) {
            for (Term term : terms) {
                termService.updateTermStatus(term, 2L);
            }
        }
    }

    @Scheduled(cron = "0 00 00 * * *") // Execute at 12:00 AM every day
    @Transactional
    @Async
    public void endTerm() throws Exception {
        //START TERM
        List<Term> terms = termRepository.getListTermNeedToClose(TermCode.IN_PROGRESS, LocalDateTime.now());
        //change status to 3 (CLOSED)
        if (terms != null) {
            for (Term term : terms) {
                termService.updateTermStatus(term, 3L);

                List<FinancialPlan> listPlanNeedToClose = new ArrayList<>();
                List<FinancialPlanExpense> listExpenseNeedToClose = new ArrayList<>();
                planRepository.findAllByTermId(term.getId()).forEach(plan -> {
                    if (plan.getStatus().getCode().equals(PlanStatusCode.WAITING_FOR_REVIEWED)
                            || plan.getStatus().getCode().equals(PlanStatusCode.NEW)) {

                        // 3L - Reviewed
                        plan.setStatus(planStatusRepository.getReferenceById(3L));

                        // Get list expense have status waiting
                        planExpenseRepository.getListExpenseNeedToCloseByPlanId(plan.getId(), ExpenseStatusCode.WAITING_FOR_APPROVAL).forEach(expense -> {
                            // Change expense status from waiting to deny
                            expense.setStatus(expenseStatusRepository.getReferenceById(4L));
                            listExpenseNeedToClose.add(expense);
                        });

                        listPlanNeedToClose.add(plan);
                        // Change expense status
                        planExpenseRepository.saveAll(listExpenseNeedToClose);
                    }

                    // Create new report
                    FinancialReport report = FinancialReport.builder()
                            .name(term.getName() + "_" + plan.getDepartment().getCode() + "_" + "Report")
                            .month(LocalDate.now())
                            .term(term)
                            .build();

                    List<FinancialReportExpense> reportExpenses = new ArrayList<>();
                    // Convert planExpense to reportExpense
                    planExpenseRepository.getListExpenseLastVersionByPlanId(plan.getId()).forEach(planExpense -> {
                        reportExpenses.add(FinancialReportExpense.builder()
                                .name(planExpense.getName())
                                .unitPrice(planExpense.getUnitPrice())
                                .amount(planExpense.getAmount())
                                .projectName(planExpense.getProjectName())
                                .supplierName(planExpense.getSupplierName())
                                .pic(planExpense.getPic())
                                .note(planExpense.getNote())
                                .financialReport(report)
                                .costType(planExpense.getCostType())
//                                .status(planExpense.getStatus())
                                .build());
                    });

                    // Generate report
                    reportRepository.save(report);
                    // Generate expense of report
                    reportExpenseRepository.saveAll(reportExpenses);
                });
                // Change status waiting and new to close
                planRepository.saveAll(listPlanNeedToClose);
            }
        }
    }
}