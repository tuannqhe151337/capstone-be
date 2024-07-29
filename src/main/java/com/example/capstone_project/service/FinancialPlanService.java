package com.example.capstone_project.service;

import com.example.capstone_project.entity.FinancialPlan;
import com.example.capstone_project.entity.*;
import com.example.capstone_project.repository.result.PlanDetailResult;
import org.springframework.data.domain.Pageable;
import com.example.capstone_project.repository.result.VersionResult;

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

    byte[] getBodyFileExcelXLSX(Long fileId) throws Exception;

    String generateXLSXFileName(Long fileId);

    String generateXLSFileName(Long fileId);

    void approvalExpenses(Long planId, List<Long> listExpenses) throws Exception;

    List<FinancialPlanExpense> getListExpenseWithPaginate(Long planId, String query, Long statusId, Long costTypeId, Pageable pageable) throws Exception;

    long countDistinctListExpenseWithPaginate(String query, Long planId, Long statusId, Long costTypeId);

    List<VersionResult> getListVersionWithPaginate(Long planId, Pageable pageable) throws Exception;

    long countDistinctListPlanVersionPaging(Long planId);
}
