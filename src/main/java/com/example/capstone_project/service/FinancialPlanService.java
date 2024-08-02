package com.example.capstone_project.service;

import com.example.capstone_project.entity.FinancialPlan;
import com.example.capstone_project.entity.*;
import com.example.capstone_project.controller.body.plan.reupload.ReUploadExpenseBody;
import com.example.capstone_project.repository.result.PlanDetailResult;
import org.springframework.data.domain.Pageable;
import com.example.capstone_project.repository.result.VersionResult;

import java.io.IOException;
import java.util.List;

public interface FinancialPlanService {
    long countDistinct(String query, Long termId, Long departmentId) throws Exception;

    List<FinancialPlan> getPlanWithPagination(String query, Long termId, Long departmentId, Integer page, Integer size, String sortBy, String sortType) throws Exception;

    FinancialPlan creatPlan(FinancialPlan plan, Term term) throws Exception;

    UserDetail getUserDetail() throws Exception;

    Term getTermById(Long termId);

    List<ReportStatus> getListPlanStatus();

    FinancialPlan deletePlan(long planId);

    PlanDetailResult getPlanDetailByPlanId(Long planId) throws Exception;

    int getPlanVersionById(Long planId);

    List<ExpenseStatus> getListExpenseStatus();

    byte[] getBodyFileExcelXLS(Long fileId) throws Exception;

    byte[] getBodyFileExcelXLSX(Long fileId) throws Exception;

    String generateXLSXFileName(Long fileId);

    String generateXLSFileName(Long fileId);

    void approvalExpenses(Long planId, List<Long> listExpenses) throws Exception;

    List<FinancialPlanExpense> getListExpenseWithPaginate(Long planId, String query, Long statusId, Long costTypeId, Pageable pageable) throws Exception;

    long countDistinctListExpenseWithPaginate(String query, Long planId, Long statusId, Long costTypeId);

    List<VersionResult> getListVersionWithPaginate(Long planId, Pageable pageable) throws Exception;

    long countDistinctListPlanVersionPaging(Long planId);

    FinancialPlan convertListExpenseAndMapToPlan(Long planId, List<ReUploadExpenseBody> expenseBodies) throws Exception;

    void reUploadPlan(FinancialPlan plan);

    byte[] getLastVersionBodyFileExcelXLS(Long planId) throws Exception;

    String generateXLSFileNameByPlanId(Long planId);

    byte[] getLastVersionBodyFileExcelXLSX(Long planId) throws Exception;

    String generateXLSXFileNameByPlanId(Long planId);

    void denyExpenses(Long planId, List<Long> listExpenseId) throws Exception;

    void approvalAllExpenses(Long planId) throws Exception;

    void submitPlanForReview(Long planId) throws Exception;

    byte[] getTemplateData() throws IOException;

}
