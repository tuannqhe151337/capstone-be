package com.example.capstone_project.service;

import com.example.capstone_project.controller.body.plan.create.NewPlanBody;
import com.example.capstone_project.entity.FinancialPlan;
import com.example.capstone_project.entity.FinancialPlanExpense;
import com.example.capstone_project.repository.result.PlanDetailResult;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
public interface FinancialPlanService {
    long countDistinct(String query, Long termId, Long departmentId, Long statusId) throws Exception;

    List<FinancialPlan> getPlanWithPagination(String query, Long termId, Long departmentId, Long statusId, Integer page, Integer size, String sortBy, String sortType) throws Exception;

    void creatPlan(NewPlanBody planBody) throws Exception;

    PlanDetailResult getPlanDetailByPlanId(Long planId);

    int getPlanVersionById(Long planId);

    List<FinancialPlanExpense> getListExpenseWithPaginate(Long planId, String query, Long statusId, Long costTypeId, Pageable pageable) throws Exception;

    long countDistinctListExpenseWithPaginate(String query, Long planId, Long statusId, Long costTypeId);
}
