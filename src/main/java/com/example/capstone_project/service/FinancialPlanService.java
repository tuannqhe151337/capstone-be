package com.example.capstone_project.service;

import com.example.capstone_project.controller.body.expense.ApprovalExpenseBody;
import com.example.capstone_project.entity.FinancialPlan;
import com.example.capstone_project.repository.result.PlanDetailResult;
import com.example.capstone_project.entity.Term;
import com.example.capstone_project.entity.UserDetail;
import com.example.capstone_project.entity.PlanStatus;

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

    void approvalExpenses(Long planId, List<Long> listExpenses) throws Exception;

    void denyExpenses(Long planId, List<Long> listExpenseId) throws Exception;

    void approvalAllExpenses(Long planId) throws Exception;
}
