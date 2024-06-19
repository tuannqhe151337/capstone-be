package com.example.capstone_project.service;

import com.example.capstone_project.controller.body.plan.create.NewPlanBody;
import com.example.capstone_project.entity.FinancialPlan;
import com.example.capstone_project.entity.FinancialPlanExpense;
import com.example.capstone_project.entity.Term;
import com.example.capstone_project.entity.UserDetail;

import java.util.List;

public interface FinancialPlanService {
    long countDistinct(String query, Long termId, Long departmentId, Long statusId) throws Exception;

    List<FinancialPlan> getPlanWithPagination(String query, Long termId, Long departmentId, Long statusId, Integer page, Integer size, String sortBy, String sortType) throws Exception;

    FinancialPlan creatPlan(FinancialPlan plan) throws Exception;

    UserDetail getUserDetail() throws Exception;

    Term getTermById(Long termId);
}
