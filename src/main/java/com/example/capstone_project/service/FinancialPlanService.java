package com.example.capstone_project.service;

import com.example.capstone_project.controller.responses.plan.StatusResponse;
import com.example.capstone_project.entity.FinancialPlan;
import com.example.capstone_project.entity.PlanStatus;

import java.util.List;

public interface FinancialPlanService {
    long countDistinct(String query, Long termId, Long departmentId, Long statusId);

    List<FinancialPlan> getPlanWithPagination(String query, Long termId, Long departmentId, Long statusId, Integer page, Integer size, String sortBy, String sortType) throws Exception;

    List<PlanStatus> getListPlanStatus();
}
