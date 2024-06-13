package com.example.capstone_project.service;

import com.example.capstone_project.entity.AccessTokenClaim;
import com.example.capstone_project.entity.FinancialPlan;
import com.example.capstone_project.entity.FinancialPlanExpense;
import com.example.capstone_project.repository.result.PlanDetailResult;
import org.springframework.stereotype.Service;

import java.util.List;
public interface FinancialPlanService {
    void creatPlan(FinancialPlan plan, List<FinancialPlanExpense> expenseList, AccessTokenClaim tokenClaim);

    PlanDetailResult getPlanDetailByPlanId(Long planId);
}
