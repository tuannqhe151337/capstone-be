package com.example.capstone_project.service.impl;

import com.example.capstone_project.controller.body.plan.reupload.ReUploadExpenseBody;
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
import com.example.capstone_project.repository.result.VersionResult;
import com.example.capstone_project.service.FinancialPlanService;
import com.example.capstone_project.utils.enums.AuthorityCode;
import com.example.capstone_project.utils.enums.ExpenseStatusCode;
import com.example.capstone_project.utils.enums.RoleCode;
import com.example.capstone_project.utils.enums.TermCode;
import com.example.capstone_project.utils.exception.InvalidInputException;
import com.example.capstone_project.utils.exception.ResourceNotFoundException;
import com.example.capstone_project.utils.exception.UnauthorizedException;
import com.example.capstone_project.utils.exception.term.InvalidDateException;
import com.example.capstone_project.utils.helper.HandleFileHelper;
import com.example.capstone_project.utils.helper.PaginationHelper;
import com.example.capstone_project.utils.helper.UserHelper;
import com.example.capstone_project.utils.mapper.plan.reupload.ReUploadExpensesMapperImpl;
import lombok.RequiredArgsConstructor;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.FileInputStream;
import java.time.LocalDateTime;
import java.util.*;
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
    private final ExpenseStatusRepository expenseStatusRepository;
    private final DepartmentRepository departmentRepository;
    private final HandleFileHelper handleFileHelper;

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
                PlanDetailResult planResult = planRepository.getFinancialPlanById(planId);

                if (planResult == null) {
                    throw new ResourceNotFoundException("Not found plan id = " + planId);
                }

                return planResult;
                // Financial staff can only view plan of their department
            } else if (userDetail.getRoleCode().equals(RoleCode.FINANCIAL_STAFF.getValue())) {
                PlanDetailResult planResult = planRepository.getFinancialPlanById(planId);

                if (planResult == null) {
                    throw new ResourceNotFoundException("Not found plan id = " + planId);
                }

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
    public List<VersionResult> getListVersionWithPaginate(Long planId, Pageable pageable) throws Exception {
        // Get userId from token
        long userId = UserHelper.getUserId();

        // Get user detail
        UserDetail userDetail = userDetailRepository.get(userId);

        // Check authority
        if (userAuthorityRepository.get(userId).contains(AuthorityCode.VIEW_PLAN.getValue())) {
            // Accountant role can view all plan
            if (userDetail.getRoleCode().equals(RoleCode.ACCOUNTANT.getValue())) {
                return planRepository.getListVersionWithPaginate(planId, pageable);

                // Financial staff can only view plan of their department
            } else if (userDetail.getRoleCode().equals(RoleCode.FINANCIAL_STAFF.getValue())) {
                FinancialPlan plan = planRepository.getReferenceById(planId);

                // Check department
                if (plan.getDepartment().getId() == userDetail.getDepartmentId()) {
                    return planRepository.getListVersionWithPaginate(planId, pageable);
                } else {
                    throw new UnauthorizedException("User can't view this plan detail because departmentId of plan not equal with departmentId of user");
                }
            } else {
                throw new UnauthorizedException("Unauthorized to view plan");
            }
        } else {
            throw new UnauthorizedException("Unauthorized to view plan");
        }
    }

    @Override
    public long countDistinctListPlanVersionPaging(Long planId) {
        return planRepository.getPlanVersionByPlanId(planId);
    }

    @Override
    public List<FinancialPlanExpense> getListExpenseWithPaginate(Long planId, String query, Long statusId, Long costTypeId, Pageable pageable) throws Exception {
        // Get userId from token
        long userId = UserHelper.getUserId();
        // Get user detail
        UserDetail userDetail = userDetailRepository.get(userId);

        // Check authority
        if (userAuthorityRepository.get(userId).contains(AuthorityCode.VIEW_PLAN.getValue())) {
            // Check exist
            if (!planRepository.existsById(planId)) {
                throw new ResourceNotFoundException("Not found any plan have id = " + planId);
            }

            // Checkout role, accountant can view all plan
            if (userDetail.getRoleCode().equals(RoleCode.ACCOUNTANT.getValue())) {

                return expenseRepository.getListExpenseWithPaginate(planId, query, statusId, costTypeId, pageable);

                // But financial staff can only view plan of their department
            } else if (userDetail.getRoleCode().equals(RoleCode.FINANCIAL_STAFF.getValue())) {

                if (userDetail.getDepartmentId() == planRepository.getDepartmentIdByPlanId(planId)) {

                    return expenseRepository.getListExpenseWithPaginate(planId, query, statusId, costTypeId, pageable);
                } else {
                    throw new UnauthorizedException("User can't view this department because departmentId of plan not equal with departmentId of user");
                }
            }
        }
        throw new UnauthorizedException("Unauthorized to view plan");
    }

    @Override
    public long countDistinctListExpenseWithPaginate(String query, Long planId, Long statusId, Long costTypeId) {
        return expenseRepository.countDistinctListExpenseWithPaginate(query, planId, statusId, costTypeId);
    }

    @Override
    @Transactional
    public void approvalExpenses(Long planId, List<Long> listExpenses) throws Exception {
        // Get userId from token
        long userId = UserHelper.getUserId();

        // Get user detail
        UserDetail userDetail = userDetailRepository.get(userId);

        // Check authority
        if (userAuthorityRepository.get(userId).contains(AuthorityCode.APPROVE_PLAN.getValue()) && userDetail.getRoleCode().equals(RoleCode.ACCOUNTANT.getValue())) {
            List<FinancialPlanExpense> expenses = new ArrayList<>();
            // Check list expense in one file
            long totalExpense = expenseRepository.countTotalExpenseInPlanLastVersion(planId, listExpenses, TermCode.IN_PROGRESS, LocalDateTime.now());
            if (listExpenses.size() == totalExpense) {

                listExpenses.forEach(expense -> {
                    if (!expenseRepository.existsById(expense)) {
                        throw new ResourceNotFoundException("Not found expense have id = " + expense);
                    } else {
                        FinancialPlanExpense updateExpense = expenseRepository.getReferenceById(expense);
                        updateExpense.setStatus(expenseStatusRepository.getReferenceById(3L));
                        expenses.add(updateExpense);
                    }
                });
                expenseRepository.saveAll(expenses);
                // Get plan of this list expense
                FinancialPlan plan = planRepository.getReferenceById(planId);
                // Change status to Reviewed
                plan.setStatus(planStatusRepository.getReferenceById(3L));

                planRepository.save(plan);
            } else {
                throw new InvalidInputException("List expense Id invalid ");
            }
        } else {
            throw new UnauthorizedException("Unauthorized to approval plan");
        }
    }

    @Override
    public byte[] getBodyFileExcelXLS(Long fileId) throws Exception {
        // Checkout authority and get list expenses by file id
        List<ExpenseResult> expenses = getListExpenseByFileId(fileId);

        if (expenses != null && !expenses.isEmpty()) {

            String fileLocation = "src/main/resources/fileTemplate/Financial Planning_v1.0.xls";
            FileInputStream file = new FileInputStream(fileLocation);
            HSSFWorkbook wb = new HSSFWorkbook(file);

            return handleFileHelper.fillDataToExcel(wb, expenses);
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

            return handleFileHelper.fillDataToExcel(wb, expenses);
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

        if (fileNameResultList == null) {
            throw new ResourceNotFoundException("Not exist file id = " + fileId);
        }

        for (FileNameResult fileName : fileNameResultList) {
            if (Objects.equals(fileName.getFileId(), fileId)) {
                return fileName.getTermName() + "_" + fileName.getDepartmentCode() + "_v" + fileName.getVersion() + ".xls";
            }
        }
        return null;
    }

    @Override
    public FinancialPlan convertListExpenseAndMapToPlan(Long planId, List<ReUploadExpenseBody> expenseBodies) throws Exception {

        // Get userId from token
        long userId = UserHelper.getUserId();

        // Get user detail
        UserDetail userDetail = userDetailRepository.get(userId);

        // Check authorization
        // Check any plan of user department is existing in this term
        if (userAuthorityRepository.get(userId).contains(AuthorityCode.RE_UPLOAD_PLAN.getValue())) {

            // Check exist
            if (!planRepository.existsById(planId)) {
                throw new ResourceNotFoundException("Not found any plan have id = " + planId);
            }

            long departmentId = planRepository.getDepartmentIdByPlanId(planId);
            // Check department
            if (departmentId == userDetail.getDepartmentId()) {
                List<ExpenseResult> listExpenseCreate = expenseRepository.getListExpenseByPlanId(planId);

                // Check null and empty
                if (listExpenseCreate == null || listExpenseCreate.isEmpty()) {
                    throw new ResourceNotFoundException("List expense is empty");
                }

                // Handle list expense
                HashMap<String, ExpenseStatusCode> hashMapExpense = new HashMap<>();
                List<FinancialPlanExpense> listExpense = new ArrayList<>();

                // Get last code of expense by plan Id
                String lastExpenseCode = expenseRepository.getLastExpenseCode(planId);
                String[] parts = lastExpenseCode.split("_");

                // Create prefix expense code
                StringBuilder prefixExpenseKey = new StringBuilder();
                for (int i = 0; i < parts.length - 2; i++) {
                    prefixExpenseKey.append(parts[i] + "_");
                }

                int lastIndexCode = Integer.parseInt(parts[parts.length - 1]);

                // Get current version by plan Id
                PlanVersionResult version = planRepository.getCurrentVersionByPlanId(planId);

                // Split list expense depend on status code
                for (ExpenseResult expenseResult : listExpenseCreate) {
                    if (expenseResult.getStatusCode().equals(ExpenseStatusCode.APPROVED)) {
                        hashMapExpense.putIfAbsent(expenseResult.getExpenseCode(), ExpenseStatusCode.APPROVED);

                        // Add old expenses had approved in old version
                        listExpense.add(expenseRepository.getReferenceById(expenseResult.getExpenseId()));
                    } else if (expenseResult.getStatusCode().equals(ExpenseStatusCode.NEW)) {
                        hashMapExpense.putIfAbsent(expenseResult.getExpenseCode(), ExpenseStatusCode.NEW);
                    } else if (expenseResult.getStatusCode().equals(ExpenseStatusCode.WAITING_FOR_APPROVAL)) {
                        hashMapExpense.putIfAbsent(expenseResult.getExpenseCode(), ExpenseStatusCode.WAITING_FOR_APPROVAL);
                    } else if (expenseResult.getStatusCode().equals(ExpenseStatusCode.DENIED)) {
                        hashMapExpense.putIfAbsent(expenseResult.getExpenseCode(), ExpenseStatusCode.DENIED);
                    }
                }

                // Handle new expenses need to re-upload
                for (ReUploadExpenseBody expenseBody : expenseBodies) {

                    // If exist expense code and status not approve, update expense
                    if (hashMapExpense.containsKey(expenseBody.getExpenseCode()) &&
                            !hashMapExpense.get(expenseBody.getExpenseCode()).getValue().equals(ExpenseStatusCode.APPROVED.getValue())
                    ) {
                        listExpense.add(new ReUploadExpensesMapperImpl().mapUpdateExpenseToPlanExpense(expenseBody));

                        // If not exist expense code, create new expense
                    } else if (!hashMapExpense.containsKey(expenseBody.getExpenseCode())) {
                        listExpense.add(new ReUploadExpensesMapperImpl().newExpenseToPlanExpense(expenseBody, prefixExpenseKey, version.getVersion(), ++lastIndexCode));
                    }
                }

                // Map to plan
                FinancialPlan plan = new ReUploadExpensesMapperImpl().mapToPlanMapping(planId, (long) UserHelper.getUserId(), version, listExpense);

                return plan;
            } else {
                throw new UnauthorizedException("User can't upload this plan because departmentId of plan not equal with departmentId of user");
            }
        } else {
            throw new UnauthorizedException("Unauthorized to re-upload plan");
        }
    }

    @Override
    @Transactional
    public void reUploadPlan(FinancialPlan plan) {
        planRepository.save(plan);
    }


    @Override
    public byte[] getLastVersionBodyFileExcelXLS(Long planId) throws Exception {
        // Checkout authority and get list expenses by file id
        List<ExpenseResult> expenses = getListExpenseByPlanId(planId);

        if (expenses != null) {

            String fileLocation = "src/main/resources/fileTemplate/Financial Planning_v1.0.xls";
            FileInputStream file = new FileInputStream(fileLocation);
            HSSFWorkbook wb = new HSSFWorkbook(file);

            return handleFileHelper.fillDataToExcel(wb, expenses);
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

            return handleFileHelper.fillDataToExcel(wb, expenses);
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
}
