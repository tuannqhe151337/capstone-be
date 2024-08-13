package com.example.capstone_project.service;

import com.example.capstone_project.entity.FinancialPlan;
import com.example.capstone_project.entity.*;
import com.example.capstone_project.repository.result.PlanDetailResult;
import com.example.capstone_project.repository.result.UserDownloadResult;
import com.example.capstone_project.repository.result.YearDiagramResult;
import com.example.capstone_project.service.result.CostResult;
import com.example.capstone_project.utils.exception.term.InvalidDateException;
import org.springframework.data.domain.Pageable;
import com.example.capstone_project.repository.result.VersionResult;

import java.io.IOException;
import java.util.List;

public interface FinancialPlanService {
    long countDistinct(String query, Long termId, Long departmentId) throws Exception;

    List<FinancialPlan> getPlanWithPagination(String query, Long termId, Long departmentId, Integer page, Integer size, String sortBy, String sortType) throws Exception;

    FinancialPlan createPlan(FinancialPlan plan, List<FinancialPlanExpense> expenses, String fileName, Long termId) throws Exception;

    UserDetail getUserDetail() throws Exception;

    Term getTermById(Long termId);

    List<ReportStatus> getListPlanStatus();

    FinancialPlan deletePlan(long planId) throws InvalidDateException;

    PlanDetailResult getPlanDetailByPlanId(Long planId) throws Exception;

    int getPlanVersionById(Long planId);

    List<ExpenseStatus> getListExpenseStatus();

    byte[] getBodyFileExcelXLS(Long fileId) throws Exception;

    byte[] getBodyFileExcelXLSX(Long fileId) throws Exception;

    String generateXLSXFileName(Long fileId);

    String generateXLSFileName(Long fileId);

//    void approvalExpenses(Long planId, List<Long> listExpenses) throws Exception;

    List<FinancialPlanExpense> getListExpenseWithPaginate(Long planId, String query, Long statusId, Long costTypeId, Long projectId, Long supplierId, Long picId, Long currencyId, Pageable pageable) throws Exception;

    long countDistinctListExpenseWithPaginate(String query, Long planId, Long statusId, Long costTypeId, Long projectId, Long supplierId, Long picId);

    List<VersionResult> getListVersionWithPaginate(Long planId, Pageable pageable) throws Exception;

    long countDistinctListPlanVersionPaging(Long planId);

    FinancialPlan convertListExpenseAndMapToPlan(Long planId, List<FinancialPlanExpense> reUploadExpenses) throws Exception;

    void reUploadPlan(FinancialPlan plan);

    byte[] getLastVersionBodyFileExcelXLS(Long planId) throws Exception;

    String generateXLSFileNameByPlanId(Long planId);

    byte[] getLastVersionBodyFileExcelXLSX(Long planId) throws Exception;

    String generateXLSXFileNameByPlanId(Long planId);

//    void denyExpenses(Long planId, List<Long> listExpenseId) throws Exception;

//    void approvalAllExpenses(Long planId) throws Exception;

    void submitPlanForReview(Long planId) throws Exception;

    byte[] getTemplateData() throws IOException;

    List<UserDownloadResult> checkUsernameExist(List<String> listUsername) throws Exception;
    CostResult calculateActualCostByPlanId(Long planId) throws Exception;

    CostResult calculateExpectedCostByPlanId(Long planId) throws Exception;

    List<YearDiagramResult> generateYearDiagram(Integer year) throws Exception;
}
