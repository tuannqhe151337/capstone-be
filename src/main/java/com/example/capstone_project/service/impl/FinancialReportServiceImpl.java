package com.example.capstone_project.service.impl;

import com.example.capstone_project.controller.responses.CustomSort;
import com.example.capstone_project.controller.responses.report.approval.ExpenseCodeResponse;
import com.example.capstone_project.entity.*;
import com.example.capstone_project.entity.Currency;
import com.example.capstone_project.repository.*;
import com.example.capstone_project.repository.redis.UserAuthorityRepository;
import com.example.capstone_project.repository.redis.UserDetailRepository;
import com.example.capstone_project.repository.result.*;
import com.example.capstone_project.service.FinancialReportService;
import com.example.capstone_project.service.result.CostResult;
import com.example.capstone_project.service.result.TotalCostByCurrencyResult;
import com.example.capstone_project.utils.enums.*;
import com.example.capstone_project.utils.exception.InvalidInputException;
import com.example.capstone_project.utils.exception.UnauthorizedException;
import com.example.capstone_project.utils.exception.ResourceNotFoundException;
import com.example.capstone_project.utils.helper.HandleFileHelper;
import com.example.capstone_project.utils.helper.PaginationHelper;
import com.example.capstone_project.utils.helper.RemoveDuplicateHelper;
import com.example.capstone_project.utils.helper.UserHelper;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import lombok.RequiredArgsConstructor;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.FileInputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@RequiredArgsConstructor
public class FinancialReportServiceImpl implements FinancialReportService {
    private final UserAuthorityRepository userAuthorityRepository;
    private final UserDetailRepository userDetailRepository;
    private final FinancialReportRepository financialReportRepository;
    private final FinancialPlanExpenseRepository expenseRepository;
    private final DepartmentRepository departmentRepository;
    private final CostTypeRepository costTypeRepository;
    private final ExpenseStatusRepository expenseStatusRepository;
    private final ReportStatusRepository reportStatusRepository;
    private final HandleFileHelper handleFileHelper;
    private final ProjectRepository projectRepository;
    private final SupplierRepository supplierRepository;
    private final CurrencyRepository currencyRepository;
    private final CurrencyExchangeRateRepository currencyExchangeRateRepository;
    private final MonthlyReportSummaryRepository monthlyReportSummaryRepository;
    private final FirebaseMessaging firebaseMessaging;
    private final UserRepository userRepository;

    @Override
    public List<FinancialReport> getListReportPaginate(String query, Long termId, Long departmentId, Long statusId, Integer pageInt, Integer sizeInt, String sortBy, String sortType) throws Exception {
        // Get userId from token
        long userId = UserHelper.getUserId();

        // Check authority
        if (userAuthorityRepository.get(userId).contains(AuthorityCode.VIEW_REPORT.getValue())) {
            Pageable pageable = null;
            if (sortBy == null || sortBy.isEmpty()) {
                pageable = PaginationHelper.handlingPaginationWithMultiSort(pageInt, sizeInt, List.of(
                        CustomSort.builder().sortBy(FinancialReport_.STATUS).sortType("asc").build(),
                        CustomSort.builder().sortBy(FinancialReport_.CREATED_AT).sortType("desc").build(),
                        CustomSort.builder().sortBy(FinancialReport_.ID).sortType("desc").build()
                ));
            } else {
                // Sort by request
                if (sortBy.equals("id") || sortBy.equals("ID")) {
                    // Sort by id
                    pageable = PaginationHelper.handlingPaginationWithMultiSort(pageInt, sizeInt, List.of(
                            CustomSort.builder().sortBy(sortBy).sortType(sortType).build()
                    ));

                } else {
                    pageable = PaginationHelper.handlingPaginationWithMultiSort(pageInt, sizeInt, List.of(
                            CustomSort.builder().sortBy(sortBy).sortType(sortType).build(),
                            CustomSort.builder().sortBy(FinancialReport_.ID).sortType("desc").build()
                    ));
                }
            }
            return financialReportRepository.getReportWithPagination(query, termId, statusId, pageable);
        } else {
            throw new UnauthorizedException("Unauthorized to view report");
        }

    }

    @Override
    public long countDistinctListReportPaginate(String query, Long termId, Long statusId) throws Exception {
        // Get userId from token
        long userId = UserHelper.getUserId();

        // Check authority or role
        if (userAuthorityRepository.get(userId).contains(AuthorityCode.VIEW_REPORT.getValue())) {
            return financialReportRepository.countDistinctListReportPaginate(query, termId, statusId);
        } else {
            throw new UnauthorizedException("Unauthorized to create plan");
        }

    }

    @Override
    public ReportDetailResult getReportDetailByReportId(Long reportId) throws Exception {
        // Get userId from token
        long userId = UserHelper.getUserId();

        // Check authority
        if (userAuthorityRepository.get(userId).contains(AuthorityCode.VIEW_REPORT.getValue())) {

            if (!financialReportRepository.existsById(reportId)) {
                throw new ResourceNotFoundException("Not found any report have id = " + reportId);
            }

            return financialReportRepository.getFinancialReportById(reportId);

        } else {
            throw new UnauthorizedException("Unauthorized to view report");
        }
    }

    @Override
    @Transactional
    public FinancialReport deleteReport(Long reportId) {
        // Check authorization
        if (userAuthorityRepository.get(UserHelper.getUserId()).contains(AuthorityCode.DELETE_REPORT.getValue())) {
            FinancialReport financialReport = financialReportRepository.findById(reportId).orElseThrow(() ->
                    new ResourceNotFoundException("Not found any report have id = " + reportId));
            financialReport.setDelete(true);

            financialReportRepository.save(financialReport);

            return financialReport;
        } else {
            throw new ResourceNotFoundException("Unauthorized to delete report");
        }
    }

//    @Override
//    @Transactional
//    public List<FinancialReportExpense> getListExpenseWithPaginate(Long reportId, String query, Integer statusId, Integer costTypeId, Pageable pageable) throws Exception {
//        // Get userId from token
//        long userId = UserHelper.getUserId();
//        // Get user detail
//        UserDetail userDetail = userDetailRepository.get(userId);
//
//        // Check authority
//        if (userAuthorityRepository.get(userId).contains(AuthorityCode.VIEW_REPORT.getValue())) {
//            if (!financialReportRepository.existsById(reportId)) {
//                throw new ResourceNotFoundException("Not found any report have id = " + reportId);
//            }
//
//            FinancialReport report = financialReportRepository.getReferenceById(reportId);
//
//            // Checkout role, accountant can view all plan
//            if (userDetail.getRoleCode().equals(RoleCode.ACCOUNTANT.getValue())) {
//
//                return expenseRepository.getListExpenseWithPaginate(reportId, query, statusId, costTypeId, pageable);
//
//                // But financial staff can only view plan of their department
//            } else if (userDetail.getRoleCode().equals(RoleCode.FINANCIAL_STAFF.getValue())) {
//
//                if (userDetail.getDepartmentId() == report.getDepartment().getId()) {
//
//                    return expenseRepository.getListExpenseWithPaginate(reportId, query, statusId, costTypeId, pageable);
//                } else {
//
//                    throw new UnauthorizedException("User can't view this report because departmentId of plan not equal with departmentId of user");
//                }
//            }
//            throw new UnauthorizedException("Unauthorized to view report");
//        } else {
//            throw new UnauthorizedException("Unauthorized to view report");
//        }
//    }

//    @Override
//    public long countDistinctListExpenseWithPaginate(String query, Long reportId, Integer statusId, Integer costTypeId) {
//        return expenseRepository.countDistinctListExpenseWithPaginate(query, reportId, statusId, costTypeId);
//    }

    @Override
    public byte[] getBodyFileExcelXLSX(Long reportId) throws Exception {
        // Checkout authority and get list expenses by file id
        List<ExpenseResult> expenses = getListExpenseByReportId(reportId);

        if (expenses != null && !expenses.isEmpty()) {
            List<Department> departments = departmentRepository.findAll();
            List<CostType> costTypes = costTypeRepository.findAll();
            List<ExpenseStatus> expenseStatuses = expenseStatusRepository.findAll();
            List<Project> projects = projectRepository.findAll();
            List<Currency> currencies = currencyRepository.findAll();
            List<Supplier> suppliers = supplierRepository.findAll();

            String fileLocation = "src/main/resources/fileTemplate/Financial Planning_v1.0.xlsx";
            FileInputStream file = new FileInputStream(fileLocation);
            XSSFWorkbook wb = new XSSFWorkbook(file);

            return handleFileHelper.fillDataToExcel(wb, expenses, departments, costTypes, expenseStatuses, projects, suppliers, currencies);
        } else {
            throw new ResourceNotFoundException("List expenses is empty");
        }
    }

    @Override
    public String generateXLSXFileName(Long reportId) {
        FileNameResult fileNameResult = financialReportRepository.generateFileName(reportId);
        if (fileNameResult != null) {
            return fileNameResult.getTermName() + "_Report.xlsx";
        } else {
            throw new ResourceNotFoundException("Not found any report have id = " + reportId);
        }
    }

    @Override
    public byte[] getBodyFileExcelXLS(Long reportId) throws Exception {
        // Checkout authority and get list expenses by file id
        List<ExpenseResult> expenses = getListExpenseByReportId(reportId);

        if (expenses != null) {
            List<Department> departments = departmentRepository.findAll();
            List<CostType> costTypes = costTypeRepository.findAll();
            List<ExpenseStatus> expenseStatuses = expenseStatusRepository.findAll();
            List<Project> projects = projectRepository.findAll();
            List<Currency> currencies = currencyRepository.findAll();
            List<Supplier> suppliers = supplierRepository.findAll();

            String fileLocation = "src/main/resources/fileTemplate/Financial Planning_v1.0.xls";
            FileInputStream file = new FileInputStream(fileLocation);
            HSSFWorkbook wb = new HSSFWorkbook(file);

            return handleFileHelper.fillDataToExcel(wb, expenses, departments, costTypes, expenseStatuses, projects, suppliers, currencies);
        } else {
            throw new ResourceNotFoundException("List expense is null or empty");
        }
    }

    @Override
    public String generateXLSFileName(Long reportId) {
        FileNameResult fileNameResult = financialReportRepository.generateFileName(reportId);

        if (fileNameResult != null) {
            return fileNameResult.getTermName() + "_Report.xls";
        } else {
            throw new ResourceNotFoundException("Not found report have id = " + reportId);
        }
    }

    @Override
    public List<ReportExpenseResult> getListExpenseWithPaginate(Long reportId, String query, Integer departmentId, Integer statusId, Integer costTypeId, Integer projectId, Integer supplierId, Integer picId, Long toCurrencyId, Pageable pageable) throws Exception {
        // Get userId from token
        long userId = UserHelper.getUserId();

        // Check authority
        if (userAuthorityRepository.get(userId).contains(AuthorityCode.VIEW_REPORT.getValue())) {
            if (!financialReportRepository.existsById(reportId)) {
                throw new ResourceNotFoundException("Not found any report have id = " + reportId);
            }
            List<ReportExpenseResult> expenses = expenseRepository.getListExpenseForReport(reportId, query, departmentId, statusId, costTypeId, projectId, supplierId, picId, pageable);

            //Handle currency
            if (toCurrencyId != null) {
                if (!currencyRepository.existsById(toCurrencyId)) {
                    throw new ResourceNotFoundException("Currency id not exist id = " + toCurrencyId);
                }
                try {
                    // Inner hashmap: map by currency id
                    HashMap<Long, List<ReportExpenseResult>> fromCurrencyIdHashMap = new HashMap<>();

                    Set<PaginateExchange> monthYearSet = new HashSet<>();

                    expenses.forEach(expense -> {
                        fromCurrencyIdHashMap.putIfAbsent(expense.getCurrency().getId(), new ArrayList<>());
                    });

                    expenses.forEach(expense -> {
                        fromCurrencyIdHashMap.get(expense.getCurrency().getId()).add(expense);
                        monthYearSet.add(PaginateExchange.builder()
                                .month(expense.getCreatedAt().getMonthValue())
                                .year(expense.getCreatedAt().getYear())
                                .build());
                    });

                    // Get list exchange rates
                    List<Long> currencyIds = new ArrayList<>(fromCurrencyIdHashMap.keySet().stream().toList());
                    currencyIds.add(toCurrencyId);

                    List<CurrencyExchangeRate> exchangeRates = this.currencyExchangeRateRepository.getListCurrencyExchangeRateByMonthYear(monthYearSet.stream().toList(), currencyIds);

                    // Outer hashmap: map by date
                    HashMap<String, HashMap<Long, BigDecimal>> exchangeRateHashMap = new HashMap<>();

                    exchangeRates.forEach(exchangeRate -> {
                        exchangeRateHashMap.putIfAbsent(exchangeRate.getMonth().format(DateTimeFormatter.ofPattern("M/yyyy")), new HashMap<>());
                    });

                    exchangeRates.forEach(exchangeRate -> {
                        exchangeRateHashMap.get(exchangeRate.getMonth().format(DateTimeFormatter.ofPattern("M/yyyy"))).put(exchangeRate.getCurrency().getId(), exchangeRate.getAmount());
                    });

                    fromCurrencyIdHashMap.keySet().forEach(fromCurrencyId -> {
                        fromCurrencyIdHashMap.get(fromCurrencyId).forEach(expense -> {
                            BigDecimal formAmount = BigDecimal.valueOf(exchangeRateHashMap.get(expense.getCreatedAt().format(DateTimeFormatter.ofPattern("M/yyyy"))).get(fromCurrencyId).longValue());
                            BigDecimal toAmount = BigDecimal.valueOf(exchangeRateHashMap.get(expense.getCreatedAt().format(DateTimeFormatter.ofPattern("M/yyyy"))).get(toCurrencyId).longValue());
                            expense.setUnitPrice(expense.getUnitPrice().multiply(formAmount).divide(toAmount, 2, RoundingMode.CEILING));
                            expense.setCurrency(currencyRepository.getReferenceById(toCurrencyId));
                        });
                    });
                } catch (ArithmeticException e) {
                    throw new ArithmeticException("Can't divided by 0");
                } catch (NullPointerException e) {
                    throw new Exception();
                }
            }

            return expenses;
        } else {
            throw new UnauthorizedException("Unauthorized to view report");
        }
    }

    @Override
    public long countDistinctListExpenseWithPaginate(String query, Long reportId, Integer departmentId, Integer statusId, Integer costTypeId, Integer projectId, Integer supplierId, Integer picId) {
        // Get userId from token
        long userId = UserHelper.getUserId();

        // Check authority
        if (userAuthorityRepository.get(userId).contains(AuthorityCode.VIEW_REPORT.getValue())) {
            if (!financialReportRepository.existsById(reportId)) {
                throw new ResourceNotFoundException("Not found any report have id = " + reportId);
            }
            return expenseRepository.countDistinctListExpenseForReport(query, reportId, departmentId, statusId, costTypeId, projectId, supplierId, picId);
        } else {
            throw new UnauthorizedException("Unauthorized to view report");
        }
    }

    private CostResult calculateCostByPlanIdAndStatusCode(Long reportId, ExpenseStatusCode statusCode) throws Exception {
        List<TotalCostByCurrencyResult> costByCurrencyResults = financialReportRepository.calculateCostByReportIdAndStatus(reportId, statusCode);

        Currency defaultCurrency = currencyRepository.getDefaultCurrency();

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
                actualCost = actualCost.add(costByCurrency.getTotalCost().multiply(formAmount).divide(toAmount, 2, RoundingMode.CEILING));
                System.out.println(actualCost);
            }
        }

        return CostResult.builder().cost(actualCost).currency(defaultCurrency).build();

    }

    @Override
    public CostResult calculateActualCostByReportId(Long reportId) throws Exception {
        // Get userId from token
        long userId = UserHelper.getUserId();

        // Check authority
        if (userAuthorityRepository.get(userId).contains(AuthorityCode.VIEW_REPORT.getValue())) {
            if (!financialReportRepository.existsById(reportId)) {
                throw new ResourceNotFoundException("Not found any report have id = " + reportId);
            }
            return calculateCostByPlanIdAndStatusCode(reportId, ExpenseStatusCode.APPROVED);
        } else {
            throw new UnauthorizedException("Unauthorized to view report");
        }
    }

    @Override
    public CostResult calculateExpectedCostByReportId(Long reportId) throws Exception {
        // Get userId from token
        long userId = UserHelper.getUserId();

        // Check authority
        if (userAuthorityRepository.get(userId).contains(AuthorityCode.VIEW_REPORT.getValue())) {
            if (!financialReportRepository.existsById(reportId)) {
                throw new ResourceNotFoundException("Not found any report have id = " + reportId);
            }
            return calculateCostByPlanIdAndStatusCode(reportId, null);
        } else {
            throw new UnauthorizedException("Unauthorized to view report");
        }
    }

    private List<ExpenseResult> getListExpenseByReportId(Long reportId) throws Exception {
        // Get userId from token
        long userId = UserHelper.getUserId();

        // Check authority
        if (userAuthorityRepository.get(userId).contains(AuthorityCode.DOWNLOAD_REPORT.getValue())) {
            if (!financialReportRepository.existsById(reportId)) {
                throw new ResourceNotFoundException("Not found any report have id = " + reportId);
            }
            return expenseRepository.getListExpenseByReportId(reportId);
        } else {
            throw new UnauthorizedException("Unauthorized to download report");
        }
    }


    @Override
    public void approvalAllExpenses(Long reportId) throws Exception {
        // Get userId from token
        long userId = UserHelper.getUserId();

        // Get user detail
        UserDetail userDetail = userDetailRepository.get(userId);

        // Check authority
        if (userAuthorityRepository.get(userId).contains(AuthorityCode.APPROVE_PLAN.getValue()) && userDetail.getRoleCode().equals(RoleCode.ACCOUNTANT.getValue())) {
            List<FinancialPlanExpense> expenses = expenseRepository.getListExpenseToApprovedByReportId(reportId, TermStatusCode.IN_PROGRESS, LocalDateTime.now());
            if (expenses == null || expenses.isEmpty()) {
                throw new ResourceNotFoundException("Not exist report id = " + reportId + " or list expense is empty");
            }

            // Get approval status
            ExpenseStatus approval = expenseStatusRepository.findByCode(ExpenseStatusCode.APPROVED);


            expenses.forEach(expense -> {
                expense.setStatus(approval);
            });

            expenseRepository.saveAll(expenses);
            // Get plan of this list expense
            FinancialReport report = financialReportRepository.getReferenceById(reportId);
            // Change status to Reviewed

            ReportStatus reviewedReportStatus = reportStatusRepository.findByCode(ReportStatusCode.REVIEWED);

            report.setStatus(reviewedReportStatus);

            financialReportRepository.save(report);

            expenseRepository.saveAll(expenses);
        } else {
            throw new UnauthorizedException("Unauthorized to approval expense");
        }
    }

    @Override
    public void uploadReportExpenses(Long reportId, List<FinancialPlanExpense> rawExpenses) throws Exception {
        // Get userId from token
        long userId = UserHelper.getUserId();

        // Get user detail
        UserDetail userDetail = userDetailRepository.get(userId);

        // Check authority
        if (userAuthorityRepository.get(userId).contains(AuthorityCode.APPROVE_PLAN.getValue()) && userDetail.getRoleCode().equals(RoleCode.ACCOUNTANT.getValue())) {

            List<Long> listExpenseId = new ArrayList<>();

            for (FinancialPlanExpense expense : rawExpenses) {
                listExpenseId.add(expense.getId());
            }

            List<FinancialPlanExpense> expenses = new ArrayList<>();
            // Check list expense in one report
            List<ExpenseResult> expenseResults = expenseRepository.getListExpenseInReportUpload(reportId, listExpenseId, TermStatusCode.IN_PROGRESS, LocalDateTime.now());

            if (listExpenseId.size() == expenseResults.size()) {

                // Get last code in this report
                String lastCode = financialReportRepository.getLastCodeInReport(reportId);
                int index = 0;

                // If list expense not have any expense have code then index start from 1
                if (lastCode != null) {
                    // Split the string by the underscore character
                    String[] parts = lastCode.split("_");

                    // Get last index
                    index = Integer.parseInt(parts[parts.length - 1]);
                }

                HashMap<Long, String> idAndCode = new HashMap<>();

                for (ExpenseResult expenseResult : expenseResults) {
                    idAndCode.put(expenseResult.getExpenseId(), expenseResult.getExpenseCode());
                }

                ExpenseStatus approval = expenseStatusRepository.findByCode(ExpenseStatusCode.APPROVED);
                ExpenseStatus denied = expenseStatusRepository.findByCode(ExpenseStatusCode.DENIED);

                // Get user approved by
                User approvedBy = userRepository.getReferenceById(userId);

                // Get report of this list expense to generate expense code
                FinancialReport report = financialReportRepository.findById(reportId).get();

                for (FinancialPlanExpense expense : rawExpenses) {
                    FinancialPlanExpense updateExpense = expenseRepository.getReferenceById(expense.getId());

                    // Generate code for approved expense not have code
                    if (idAndCode.get(expense.getId()) == null && expense.getStatus().getCode().equals(ExpenseStatusCode.APPROVED)) {
                        String prefixCode = report.getName().replace(" ", "_");
                        updateExpense.setPlanExpenseKey(prefixCode + "_" + (++index));
                    }

                    // Update status for expense
                    if (expense.getStatus().getCode().equals(approval.getCode())) {
                        updateExpense.setStatus(approval);
                        updateExpense.setApprovedBy(approvedBy);
                    } else {
                        updateExpense.setStatus(denied);

                    }

                    expenses.add(updateExpense);
                }

                // Change status to Reviewed
//                ReportStatus reviewedReportStatus = reportStatusRepository.findByCode(ReportStatusCode.REVIEWED);
//
//                report.setStatus(reviewedReportStatus);

                financialReportRepository.save(report);
                expenseRepository.saveAll(expenses);
            } else {
                throw new InvalidInputException("List expense Id invalid ");
            }
        } else {
            throw new UnauthorizedException("Unauthorized to approval expense");
        }
    }

    @Override
    public List<YearDiagramResult> generateYearDiagram(Integer year) throws Exception {
        UserDetail userDetail = userDetailRepository.get(UserHelper.getUserId());

        if (userDetail.getRoleCode().equals(RoleCode.ACCOUNTANT.getValue())) {
            return financialReportRepository.generateYearDiagram(year);
        } else {
            throw new UnauthorizedException("Unauthorized to view diagram");
        }
    }

    @Override
    public List<CostTypeDiagramResult> getYearCostTypeDiagram(Integer year) throws Exception {
        UserDetail userDetail = userDetailRepository.get(UserHelper.getUserId());

        Long departmentId = null;
        if (userDetail.getRoleCode().equals(RoleCode.ACCOUNTANT.getValue())) {
            if (LocalDate.now().getYear() == year) {
                return monthlyReportSummaryRepository.getCostTypeYearDiagramForCurrentYear(year, departmentId);
            }
            return monthlyReportSummaryRepository.getCostTypeYearDiagram(year, departmentId);
        } else if (userDetail.getRoleCode().equals(RoleCode.FINANCIAL_STAFF.getValue())) {
            departmentId = userDetail.getDepartmentId();
            if (LocalDate.now().getYear() == year) {
                return monthlyReportSummaryRepository.getCostTypeYearDiagramForCurrentYear(year, departmentId);
            }
            return monthlyReportSummaryRepository.getCostTypeYearDiagram(year, departmentId);
        } else {
            throw new UnauthorizedException("Unauthorized to view diagram");
        }
    }

    @Override
    public List<DepartmentDiagramResult> getYearDepartmentDiagram(Integer year) throws Exception {
        UserDetail userDetail = userDetailRepository.get(UserHelper.getUserId());

        if (userDetail.getRoleCode().equals(RoleCode.ACCOUNTANT.getValue())) {
            return monthlyReportSummaryRepository.getDepartmentYearDiagram(year);
        } else {
            throw new UnauthorizedException("Unauthorized to view diagram");
        }
    }

    @Override
    public TreeMap<String, List<CostTypeDiagramResult>> getReportCostTypeDiagram(Integer year) throws Exception {
        UserDetail userDetail = userDetailRepository.get(UserHelper.getUserId());

        Long departmentId = null;
        if (userDetail.getRoleCode().equals(RoleCode.ACCOUNTANT.getValue())) {
            List<CostTypeDiagramResult> costTypeDiagramResultList = monthlyReportSummaryRepository.getReportCostTypeDiagram(year, departmentId);

            if (costTypeDiagramResultList == null) {
                return new TreeMap<>();
            }

            HashMap<String, List<CostTypeDiagramResult>> costTypeDiagramResultHashMap = new HashMap<>();

            costTypeDiagramResultList.forEach(costTypeDiagramResult -> {
                costTypeDiagramResultHashMap.putIfAbsent(costTypeDiagramResult.getMonth(), new ArrayList<>());
            });

            costTypeDiagramResultList.forEach(costTypeDiagramResult -> {
                costTypeDiagramResultHashMap.get(costTypeDiagramResult.getMonth()).add(costTypeDiagramResult);
            });

            SimpleDateFormat dateFormat = new SimpleDateFormat("M/yyyy");

            // Sorting
            TreeMap<String, List<CostTypeDiagramResult>> sortedMap = new TreeMap<>((key1, key2) -> {
                try {

                    return dateFormat.parse(key1).compareTo(dateFormat.parse(key2));
                } catch (ParseException e) {
                    throw new IllegalArgumentException(e);
                }
            });

            sortedMap.putAll(costTypeDiagramResultHashMap);

            return sortedMap;
        } else if (userDetail.getRoleCode().equals(RoleCode.FINANCIAL_STAFF.getValue())) {
            departmentId = userDetail.getDepartmentId();

            List<CostTypeDiagramResult> costTypeDiagramResultList = monthlyReportSummaryRepository.getReportCostTypeDiagram(year, departmentId);

            if (costTypeDiagramResultList == null) {
                return new TreeMap<>();
            }

            HashMap<String, List<CostTypeDiagramResult>> costTypeDiagramResultHashMap = new HashMap<>();

            costTypeDiagramResultList.forEach(costTypeDiagramResult -> {
                costTypeDiagramResultHashMap.putIfAbsent(costTypeDiagramResult.getMonth(), new ArrayList<>());
            });

            costTypeDiagramResultList.forEach(costTypeDiagramResult -> {
                costTypeDiagramResultHashMap.get(costTypeDiagramResult.getMonth()).add(costTypeDiagramResult);
            });

            SimpleDateFormat dateFormat = new SimpleDateFormat("M/yyyy");

            // Sorting
            TreeMap<String, List<CostTypeDiagramResult>> sortedMap = new TreeMap<>((key1, key2) -> {
                try {

                    return dateFormat.parse(key1).compareTo(dateFormat.parse(key2));
                } catch (ParseException e) {
                    throw new IllegalArgumentException(e);
                }
            });

            sortedMap.putAll(costTypeDiagramResultHashMap);

            return sortedMap;
        } else {
            throw new UnauthorizedException("Unauthorized to view diagram");
        }
    }

    @Override
    @Transactional
    public List<ExpenseCodeResponse> approvalExpenses(Long reportId, List<Long> listExpenseId) throws Exception {
        // Get userId from token
        long userId = UserHelper.getUserId();

        // Get user detail
        UserDetail userDetail = userDetailRepository.get(userId);

        // Check authority
        if (userAuthorityRepository.get(userId).contains(AuthorityCode.APPROVE_PLAN.getValue()) && userDetail.getRoleCode().equals(RoleCode.ACCOUNTANT.getValue())) {
            listExpenseId = RemoveDuplicateHelper.removeDuplicates(listExpenseId);

            List<FinancialPlanExpense> expenses = new ArrayList<>();
            // Check list expense exist in one report
            long totalExpense = expenseRepository.countListExpenseInReport(reportId, listExpenseId, TermStatusCode.IN_PROGRESS, LocalDateTime.now());
            if (listExpenseId.size() == totalExpense) {

                // Get approval status
                ExpenseStatus approval = expenseStatusRepository.findByCode(ExpenseStatusCode.APPROVED);

                String lastCode = financialReportRepository.getLastCodeInReport(reportId);
                int index = 0;

                if (lastCode != null) {
                    // Split the string by the underscore character
                    String[] parts = lastCode.split("_");

                    // Get last index
                    index = Integer.parseInt(parts[parts.length - 1]);

                }

                // Get user approved expense
                User approvedBy = userRepository.getReferenceById(userId);

                // Get report of this list expense
                FinancialReport report = financialReportRepository.findById(reportId).get();
                List<ExpenseCodeResponse> list = new ArrayList<>();
                for (Long expenseId : listExpenseId) {
                    FinancialPlanExpense updateExpense = expenseRepository.getReferenceById(expenseId);

                    if (updateExpense.getPlanExpenseKey() == null || updateExpense.getPlanExpenseKey().isEmpty()) {
                        // Convert ' ' to '_'
                        String prefixCode = report.getName().replace(" ", "_");
                        updateExpense.setPlanExpenseKey(prefixCode + "_" + (++index));
                        list.add(ExpenseCodeResponse.builder().expenseId(expenseId).expenseCode(updateExpense.getPlanExpenseKey()).build());
                    }

                    updateExpense.setStatus(approval);
                    updateExpense.setApprovedBy(approvedBy);
                    expenses.add(updateExpense);
                }


                // Change status to Reviewed
//                ReportStatus reviewedReportStatus = reportStatusRepository.findByCode(ReportStatusCode.REVIEWED);
//
//                report.setStatus(reviewedReportStatus);

                financialReportRepository.save(report);

                expenseRepository.saveAll(expenses);

                return list;
            } else {
                throw new InvalidInputException("List expense Id invalid ");
            }
        } else {
            throw new UnauthorizedException("Unauthorized to approval expense");
        }
    }

    @Override
    public void denyExpenses(Long reportId, List<Long> listExpenseId) throws Exception {
        // Get userId from token
        long userId = UserHelper.getUserId();

        // Get user detail
        UserDetail userDetail = userDetailRepository.get(userId);

        // Check authority
        if (userAuthorityRepository.get(userId).contains(AuthorityCode.APPROVE_PLAN.getValue()) && userDetail.getRoleCode().equals(RoleCode.ACCOUNTANT.getValue())) {
            listExpenseId = RemoveDuplicateHelper.removeDuplicates(listExpenseId);

            User denyUser = this.userRepository.getReferenceById(userId);

            List<FinancialPlanExpense> expenses = new ArrayList<>();
            // Check list expense exist in one report
            long totalExpense = expenseRepository.countListExpenseInReport(reportId, listExpenseId, TermStatusCode.IN_PROGRESS, LocalDateTime.now());
            if (listExpenseId.size() == totalExpense) {

                // Get deny status
                ExpenseStatus denyStatus = expenseStatusRepository.findByCode(ExpenseStatusCode.DENIED);

                listExpenseId.forEach(expense -> {

                    FinancialPlanExpense updateExpense = expenseRepository.getReferenceById(expense);
                    updateExpense.setStatus(denyStatus);
                    updateExpense.setApprovedBy(denyUser);
                    expenses.add(updateExpense);

                });

                // Get report of this list expense
//                FinancialReport report = financialReportRepository.getReferenceById(reportId);

                // Change status to Reviewed
//                ReportStatus reviewedReportStatus = reportStatusRepository.findByCode(ReportStatusCode.REVIEWED);

//                report.setStatus(reviewedReportStatus);

//                financialReportRepository.save(report);
                expenseRepository.saveAll(expenses);
            } else {
                throw new InvalidInputException("List expense Id invalid ");
            }
        } else {
            throw new UnauthorizedException("Unauthorized to approval expense");
        }
    }

    @Override
    public void markReportAsReviewed(Long reportId) throws Exception {
        // Get userId from token
        long userId = UserHelper.getUserId();

        // Get user detail
        UserDetail userDetail = userDetailRepository.get(userId);

        // Check authority
        if (userAuthorityRepository.get(userId).contains(AuthorityCode.APPROVE_PLAN.getValue()) && userDetail.getRoleCode().equals(RoleCode.ACCOUNTANT.getValue())) {
            // Update status report to reviewed
            ReportStatus reviewedStatus = this.reportStatusRepository.findByCode(ReportStatusCode.REVIEWED);

            Optional<FinancialReport> reportOptional = this.financialReportRepository.getFinancialReportWithTerm(reportId, TermStatusCode.IN_PROGRESS, LocalDateTime.now());
            if (reportOptional.isEmpty()) {
                throw new ResourceNotFoundException("Report not found or it's not the right time to review");
            }

            FinancialReport report = reportOptional.get();
            report.setStatus(reviewedStatus);
            this.financialReportRepository.save(report);

            // Get list FCM Tokens to send to user
            List<String> fcmTokens = this.financialReportRepository.getFCMTokensOfFinancialStaffOfReport(List.of(reportId));

            List<Message> messages = new ArrayList<>();
            for (String fcmToken : fcmTokens) {
                Notification notification = Notification
                        .builder()
                        .setTitle("Plan reviewed")
                        .setBody("Your department's plan in term \"" + report.getTerm().getName() + "\" has been reviewed")
                        .build();

                Message message = Message.builder()
                        .setToken(fcmToken)
                        .setNotification(notification)
                        .build();

                messages.add(message);
            }

            // We won't do anything if sending notification failed
            try {
                if (!messages.isEmpty()) {
                    firebaseMessaging.sendEach(messages);
                }
            } catch (Exception ignored) {
            }
        }
    }

    @Override
    public List<ReportStatus> getListReportStatus() {
        // Get list authorities of user
        Set<String> authorities = userAuthorityRepository.get(UserHelper.getUserId());

        // Check authorization
        if (authorities.contains(AuthorityCode.VIEW_REPORT.getValue())) {

            return this.reportStatusRepository.findAll(Sort.by(CostType_.ID).ascending());

        } else {
            throw new UnauthorizedException("Unauthorized to view report");
        }
    }
}
