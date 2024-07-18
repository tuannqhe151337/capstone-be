package com.example.capstone_project.service;

import com.example.capstone_project.entity.*;
import com.example.capstone_project.repository.result.ExpenseResult;
import com.example.capstone_project.entity.*;
import com.example.capstone_project.repository.result.PlanDetailResult;
import com.example.capstone_project.repository.result.PlanVersionResult;
import com.example.capstone_project.repository.result.VersionResult;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface FinancialPlanService {
    long countDistinct(String query, Long termId, Long departmentId, Long statusId) throws Exception;

    List<FinancialPlan> getPlanWithPagination(String query, Long termId, Long departmentId, Long statusId, Integer page, Integer size, String sortBy, String sortType) throws Exception;

    FinancialPlan creatPlan(FinancialPlan plan, Term term) throws Exception;

    UserDetail getUserDetail() throws Exception;

    Term getTermById(Long termId);

    List<PlanStatus> getListPlanStatus();

    FinancialPlan deletePlan(long planId);

    PlanDetailResult getPlanDetailByPlanId(Long planId) throws Exception;

    int getPlanVersionById(Long planId);

    byte[] getBodyFileExcelXLS(Long fileId) throws Exception;

    List<VersionResult> getListVersionWithPaginate(Long planId, Pageable pageable) throws Exception;

    long countDistinctListPlanVersionPaging(Long planId);

    List<FinancialPlanExpense> getListExpenseWithPaginate(Long planId, String query, Long statusId, Long costTypeId, Pageable pageable) throws Exception;

    long countDistinctListExpenseWithPaginate(String query, Long planId, Long statusId, Long costTypeId);

    byte[] getBodyFileExcelXLSX(Long fileId) throws Exception;

    String generateXLSXFileName(Long fileId);

    String generateXLSFileName(Long fileId);

    List<ExpenseResult> getListExpenseByPlanId(Long planId) throws Exception;

    String getLastExpenseCode(Long planId);

    PlanVersionResult getCurrentVersionByPlanId(Long planId);

    void reUploadPlan(FinancialPlan plan);

    FinancialPlanExpense getPlanExpenseReferenceById(Long expenseId);
}
