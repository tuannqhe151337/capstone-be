package com.example.capstone_project.service.impl;

import com.example.capstone_project.controller.responses.CustomSort;
import com.example.capstone_project.entity.*;
import com.example.capstone_project.entity.FinancialPlan;
import com.example.capstone_project.entity.FinancialPlan_;
import com.example.capstone_project.entity.UserDetail;
import com.example.capstone_project.repository.*;
import com.example.capstone_project.repository.redis.UserAuthorityRepository;
import com.example.capstone_project.repository.redis.UserDetailRepository;
import com.example.capstone_project.repository.result.ExpenseResult;
import com.example.capstone_project.repository.result.FileNameResult;
import com.example.capstone_project.repository.result.PlanDetailResult;
import com.example.capstone_project.repository.result.PlanVersionResult;
import com.example.capstone_project.service.FinancialPlanService;
import com.example.capstone_project.utils.enums.AuthorityCode;
import com.example.capstone_project.utils.enums.PlanStatusCode;
import com.example.capstone_project.utils.enums.RoleCode;
import com.example.capstone_project.utils.exception.ResourceNotFoundException;
import com.example.capstone_project.utils.exception.UnauthorizedException;
import com.example.capstone_project.utils.exception.term.InvalidDateException;
import com.example.capstone_project.utils.helper.PaginationHelper;
import com.example.capstone_project.utils.helper.UserHelper;
import com.example.capstone_project.utils.mapper.plan.create.CreatePlanMapperImpl;
import lombok.RequiredArgsConstructor;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class FinancialPlanServiceImpl implements FinancialPlanService {
    private final FinancialPlanRepository planRepository;
    private final PlanStatusRepository planStatusRepository;
    private final UserAuthorityRepository userAuthorityRepository;
    private final UserDetailRepository userDetailRepository;
    private final TermRepository termRepository;
    private final FinancialPlanFileRepository financialPlanFileRepository;
    private final FinancialPlanExpenseRepository expenseRepository;
    private final DepartmentRepository departmentRepository;
    private final ExpenseStatusRepository expenseStatusRepository;

    @Override
    public long countDistinct(String query, Long termId, Long departmentId, Long statusId) throws Exception {
        // Get userId from token
        long userId = UserHelper.getUserId();

        // Get user detail
        UserDetail userDetail = userDetailRepository.get(userId);

        // Check authority or role
        if (userAuthorityRepository.get(userId).contains(AuthorityCode.VIEW_PLAN.getValue())
                && userDetail.getRoleCode().equals(RoleCode.FINANCIAL_STAFF.getValue())) {
            departmentId = userDetail.getDepartmentId();
        }

        return planRepository.countDistinct(query, termId, departmentId, statusId);
    }

    @Override
    @Transactional
    public List<FinancialPlan> getPlanWithPagination(String query, Long termId, Long departmentId, Long statusId, Integer pageInt, Integer sizeInt, String sortBy, String sortType) throws Exception {
        // Get userId from token
        long userId = UserHelper.getUserId();

        // Get user detail
        UserDetail userDetail = userDetailRepository.get(userId);

        // Check authority
        if (!userAuthorityRepository.get(userId).contains(AuthorityCode.VIEW_PLAN.getValue())) {
            throw new UnauthorizedException("Unauthorized to view plan");
        } else {

            // Handling pagination
            Pageable pageable = null;
            if (sortBy == null || sortBy.isEmpty()) {
                if (userDetail.getRoleCode().equals(RoleCode.ACCOUNTANT.getValue())) {
                    pageable = PaginationHelper.handlingPaginationWithMultiSort(pageInt, sizeInt, List.of(
                            CustomSort.builder().sortBy(RoleCode.ACCOUNTANT.toString()).sortType("").build(),
                            CustomSort.builder().sortBy(FinancialPlan_.UPDATED_AT).sortType("desc").build(),
                            CustomSort.builder().sortBy(FinancialPlan_.ID).sortType("desc").build()
                    ));
                } else if (userDetail.getRoleCode().equals(RoleCode.FINANCIAL_STAFF.getValue())) {
                    // Default sort of financial staff role
                    pageable = PaginationHelper.handlingPaginationWithMultiSort(pageInt, sizeInt, List.of(
                            CustomSort.builder().sortBy(RoleCode.FINANCIAL_STAFF.toString()).sortType("").build(),
                            CustomSort.builder().sortBy(FinancialPlan_.UPDATED_AT).sortType("desc").build(),
                            CustomSort.builder().sortBy(FinancialPlan_.ID).sortType("desc").build()
                    ));

                    // Financial staff only see list-plan of their department
                    departmentId = userDetail.getDepartmentId();
                }
            } else {
                // Sort by request
                pageable = PaginationHelper.handlingPaginationWithMultiSort(pageInt, sizeInt, List.of(
                        CustomSort.builder().sortBy(sortBy).sortType(sortType).build(),
                        CustomSort.builder().sortBy(FinancialPlan_.ID).sortType("desc").build()
                ));
            }

            List<FinancialPlan> listResult = planRepository.getPlanWithPagination(query, termId, departmentId, statusId, pageable);
            List<PlanVersionResult> listVersions = planRepository.getListPlanVersion(query, termId, departmentId, statusId);

            listResult.forEach(plan ->
                    {
                        listVersions.forEach(
                                version -> {
                                    if (Objects.equals(plan.getId(), version.getPlanId())) {
                                        plan.setVersion(version.getVersion());
                                    }
                                }
                        );
                    }
            );

            return listResult;
        }
    }

    @Override
    public List<PlanStatus> getListPlanStatus() {
        // Get list authorities of user
        Set<String> authorities = userAuthorityRepository.get(UserHelper.getUserId());

        // Check authorization
        if (authorities.contains(AuthorityCode.VIEW_PLAN.getValue())) {

            return planStatusRepository.findAll(Sort.by(CostType_.ID).ascending());

        } else {
            throw new UnauthorizedException("Unauthorized to view plan");
        }

    }

    @Override
    @Transactional
    public FinancialPlan creatPlan(FinancialPlan plan, Term term) throws Exception {
        // Get userId from token
        long userId = UserHelper.getUserId();

        // Get user detail
        UserDetail userDetail = userDetailRepository.get(userId);

        // Check authorization
        // Check any plan of user department is existing in this term
        if (!userAuthorityRepository.get(userId).contains(AuthorityCode.IMPORT_PLAN.getValue())) {
            throw new UnauthorizedException("Unauthorized to create plan");
        }
        if (termRepository.existsPlanOfDepartmentInTerm(userDetail.getDepartmentId(), plan.getTerm().getId())) {
            throw new DuplicateKeyException("This term already have plan of department id = " + userDetail.getDepartmentId());
        }
        if (!LocalDateTime.now().isBefore(term.getPlanDueDate())) {
            throw new InvalidDateException("Plan due date of this term was expired");
        }
        return planRepository.save(plan);
    }

    public UserDetail getUserDetail() throws Exception {
        return userDetailRepository.get(UserHelper.getUserId());
    }

    @Override
    public Term getTermById(Long termId) {
        termRepository.findById(termId).orElseThrow(() ->
                new ResourceNotFoundException("Not found any term have id = " + termId));

        return termRepository.getReferenceById(termId);
    }

    @Override
    @Transactional
    public FinancialPlan deletePlan(long planId) {
        // Check authorization
        if (userAuthorityRepository.get(UserHelper.getUserId()).contains(AuthorityCode.DELETE_PLAN.getValue())) {

            FinancialPlan financialPlan = planRepository.findById(planId).orElseThrow(() ->
                    new ResourceNotFoundException("Not found any plan have id = " + planId));

            financialPlan.setDelete(true);

            planRepository.save(financialPlan);

            return financialPlan;
        } else {
            throw new UnauthorizedException("Unauthorized to delete plan");
        }
    }

    @Override
    public PlanDetailResult getPlanDetailByPlanId(Long planId) throws Exception {
        // Get userId from token
        long userId = UserHelper.getUserId();

        // Get user detail
        UserDetail userDetail = userDetailRepository.get(userId);

        // Check authority
        if (userAuthorityRepository.get(userId).contains(AuthorityCode.VIEW_PLAN.getValue())) {
            // Accountant role can view all plan
            if (userDetail.getRoleCode().equals(RoleCode.ACCOUNTANT.getValue())) {
                return planRepository.getFinancialPlanById(planId);

                // Financial staff can only view plan of their department
            } else if (userDetail.getRoleCode().equals(RoleCode.FINANCIAL_STAFF.getValue())) {
                PlanDetailResult planResult = planRepository.getFinancialPlanById(planId);

                // Check department
                if (planResult.getDepartmentId() == userDetail.getDepartmentId()) {
                    return planResult;
                } else {
                    throw new UnauthorizedException("User can't view this department because departmentId of plan not equal with departmentId of user");
                }
            } else {
                throw new UnauthorizedException("Unauthorized to view plan");
            }
        } else {
            throw new UnauthorizedException("Unauthorized to view plan");
        }
    }

    @Override
    public int getPlanVersionById(Long planId) {
        return planRepository.getPlanVersionByPlanId(planId);
    }

    @Override
    public byte[] getBodyFileExcelXLS(Long fileId) throws Exception {
        // Checkout authority and get list expenses by file id
        List<ExpenseResult> expenses = getListExpenseByFileId(fileId);

        if (expenses != null && !expenses.isEmpty()) {

            String fileLocation = "src/main/resources/fileTemplate/Financial Planning_v1.0.xls";
            FileInputStream file = new FileInputStream(fileLocation);
            HSSFWorkbook wb = new HSSFWorkbook(file);

            return fillDataToExcel(wb, expenses);
        } else {
            throw new ResourceNotFoundException("Not exist file = " + fileId + " or list expenses is empty");
        }
    }

    @Override
    public byte[] getBodyFileExcelXLSX(Long fileId) throws Exception {
        // Checkout authority and get list expenses by file id
        List<ExpenseResult> expenses = getListExpenseByFileId(fileId);

        if (expenses != null && !expenses.isEmpty()) {

            String fileLocation = "src/main/resources/fileTemplate/Financial Planning_v1.0.xlsx";
            FileInputStream file = new FileInputStream(fileLocation);
            XSSFWorkbook wb = new XSSFWorkbook(file);

            return fillDataToExcel(wb, expenses);
        } else {
            throw new ResourceNotFoundException("Not exist file = " + fileId + " or list expenses is empty");
        }
    }

    private List<ExpenseResult> getListExpenseByFileId(Long fileId) throws Exception {
        // Get userId from token
        long userId = UserHelper.getUserId();

        // Get user detail
        UserDetail userDetail = userDetailRepository.get(userId);

        // Check authority
        if (userAuthorityRepository.get(userId).contains(AuthorityCode.DOWNLOAD_PLAN.getValue())) {
            // Accountant role can view all plan
            if (userDetail.getRoleCode().equals(RoleCode.ACCOUNTANT.getValue())) {
                return planRepository.getListExpenseByFileId(fileId);

                // Financial staff can only view plan of their department
            } else if (userDetail.getRoleCode().equals(RoleCode.FINANCIAL_STAFF.getValue())) {
                long departmentId = departmentRepository.getDepartmentIdByFileId(fileId);

                // Check department
                if (departmentId == userDetail.getDepartmentId()) {
                    return planRepository.getListExpenseByFileId(fileId);
                } else {
                    throw new UnauthorizedException("User can't download this plan because departmentId of plan not equal with departmentId of user");
                }
            } else {
                throw new UnauthorizedException("Unauthorized to download plan");
            }
        } else {
            throw new UnauthorizedException("Unauthorized to download plan");
        }
    }

    private byte[] fillDataToExcel(Workbook wb, List<ExpenseResult> expenses) throws IOException {
        Sheet sheet = wb.getSheet("Expense");

        String[][] tableData = new String[expenses.size()][14];

        // Convert list expense to matrix
        for (int i = 0; i < expenses.size(); i++) {
            ExpenseResult expense = expenses.get(i);
            tableData[i][0] = expense.getExpenseCode();
            tableData[i][1] = expense.getDate().toString();
            tableData[i][2] = expense.getTerm();
            tableData[i][3] = expense.getDepartment();
            tableData[i][4] = expense.getExpense();
            tableData[i][5] = expense.getCostType();
            tableData[i][6] = expense.getUnitPrice();
            tableData[i][7] = expense.getAmount();
            tableData[i][8] = expense.getTotal();
            tableData[i][9] = expense.getProjectName();
            tableData[i][10] = expense.getSupplierName();
            tableData[i][11] = expense.getPic();
            tableData[i][12] = expense.getNote();
            tableData[i][13] = expense.getStatus();
        }

        Row row = null;
        Cell cell = null;

        int rowPosition = 2;
        int colPosition = 0;

        for (int i = 0; i < tableData.length; i++) {
            row = sheet.getRow(i + rowPosition);

            for (int j = 0; j < tableData[0].length; j++) {
                cell = row.getCell(j + colPosition);

                cell.setCellValue(tableData[i][j]);
            }
        }

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        wb.write(out);
        wb.close();
        out.close();
        return out.toByteArray();
    }

    @Override
    public String generateXLSXFileName(Long fileId) {
        int planId = planRepository.getPlanIdByFileId(fileId);
        List<FileNameResult> fileNameResultList = financialPlanFileRepository.generateFileName(planId);

        if (fileNameResultList == null) {
            throw new ResourceNotFoundException("Not exist file id = " + fileId);
        }

        for (FileNameResult fileName : fileNameResultList) {
            if (Objects.equals(fileName.getFileId(), fileId)) {
                return fileName.getTermName() + "_" + fileName.getDepartmentCode() + "_v" + fileName.getVersion() + ".xlsx";
            }
        }

        return null;
    }

    @Override
    public String generateXLSFileName(Long fileId) {
        int planId = planRepository.getPlanIdByFileId(fileId);
        List<FileNameResult> fileNameResultList = financialPlanFileRepository.generateFileName(planId);

        for (FileNameResult fileName : fileNameResultList) {
            if (Objects.equals(fileName.getFileId(), fileId)) {
                return fileName.getTermName() + "_" + fileName.getDepartmentCode() + "_v" + fileName.getVersion() + ".xls";
            }
        }
        return null;
    }

    @Override
    public byte[] getLastVersionBodyFileExcelXLS(Long planId) throws Exception {
        // Checkout authority and get list expenses by file id
        List<ExpenseResult> expenses = getListExpenseByPlanId(planId);

        if (expenses != null) {

            String fileLocation = "src/main/resources/fileTemplate/Financial Planning_v1.0.xls";
            FileInputStream file = new FileInputStream(fileLocation);
            HSSFWorkbook wb = new HSSFWorkbook(file);

            return fillDataToExcel(wb, expenses);
        }

        return null;
    }

    private List<ExpenseResult> getListExpenseByPlanId(Long planId) throws Exception {
        // Get userId from token
        long userId = UserHelper.getUserId();

        // Get user detail
        UserDetail userDetail = userDetailRepository.get(userId);

        // Check authority
        if (userAuthorityRepository.get(userId).contains(AuthorityCode.VIEW_PLAN.getValue())) {

            if (!planRepository.existsById(planId)) {
                throw new ResourceNotFoundException("Not found any plan have id = " + planId);
            }

            // Accountant role can view all plan
            if (userDetail.getRoleCode().equals(RoleCode.ACCOUNTANT.getValue())) {
                return planRepository.getListExpenseByPlanId(planId);

                // Financial staff can only view plan of their department
            } else if (userDetail.getRoleCode().equals(RoleCode.FINANCIAL_STAFF.getValue())) {
                long departmentId = departmentRepository.getDepartmentIdByPlanId(planId);

                // Check department
                if (departmentId == userDetail.getDepartmentId()) {
                    return planRepository.getListExpenseByPlanId(planId);
                } else {
                    throw new UnauthorizedException("User can't download this plan because departmentId of plan not equal with departmentId of user");
                }
            }
        }
        throw new UnauthorizedException("Unauthorized to download plan");
    }

    @Override
    public String generateXLSFileNameByPlanId(Long planId) {
        FileNameResult fileNameResult = financialPlanFileRepository.getLastVersionFileName(planId);

        if (fileNameResult != null) {
            return fileNameResult.getTermName() + "_" + fileNameResult.getDepartmentCode() + "_v" + fileNameResult.getVersion() + ".xls";
        } else {
            throw new ResourceNotFoundException("Not found any file of plan have id = " + planId);
        }
    }

    @Override
    public byte[] getLastVersionBodyFileExcelXLSX(Long planId) throws Exception {
        // Checkout authority and get list expenses by file id
        List<ExpenseResult> expenses = getListExpenseByPlanId(planId);

        if (expenses != null) {

            String fileLocation = "src/main/resources/fileTemplate/Financial Planning_v1.0.xlsx";
            FileInputStream file = new FileInputStream(fileLocation);
            XSSFWorkbook wb = new XSSFWorkbook(file);

            return fillDataToExcel(wb, expenses);
        }

        return null;
    }

    @Override
    public String generateXLSXFileNameByPlanId(Long planId) {
        FileNameResult fileNameResult = financialPlanFileRepository.getLastVersionFileName(planId);

        if (fileNameResult != null) {
            return fileNameResult.getTermName() + "_" + fileNameResult.getDepartmentCode() + "_v" + fileNameResult.getVersion() + ".xlsx";
        } else {
            throw new ResourceNotFoundException("Not found any file of plan have id = " + planId);
        }
    }

    @Override
    public void submitPlanForReview(Long planId) throws Exception {
        // Get userId from token
        long userId = UserHelper.getUserId();

        // Get user detail
        UserDetail userDetail = userDetailRepository.get(userId);
        // Check authority
        if (userAuthorityRepository.get(userId).contains(AuthorityCode.SUBMIT_PLAN_FOR_REVIEW.getValue())) {
            // Check exist
            FinancialPlan plan = planRepository.findById(planId).orElseThrow(() -> new ResourceNotFoundException("Not found any plan have id = " + planId));
            // Check department
            if (plan.getDepartment().getId() == userDetail.getDepartmentId() &&
                    plan.getStatus().getCode().equals(PlanStatusCode.NEW)) {
                List<FinancialPlanExpense> listExpenseNeedToSubmit = new ArrayList<>();
                expenseRepository.getListExpenseNewInLastVersion(planId).forEach(expense -> {
                    // Change expense status from new to waiting for approval - 2L
                    expense.setStatus(expenseStatusRepository.getReferenceById(2L));
                    listExpenseNeedToSubmit.add(expense);
                });
                expenseRepository.saveAll(listExpenseNeedToSubmit);
                // Change plan status
                plan.setStatus(planStatusRepository.getReferenceById(2L));
                planRepository.save(plan);
            } else {
                throw new UnauthorizedException("User can't submit this plan because departmentId of plan not equal with departmentId of user");
            }
        } else {
            throw new UnauthorizedException("Unauthorized to view plan");
        }
    }
}
