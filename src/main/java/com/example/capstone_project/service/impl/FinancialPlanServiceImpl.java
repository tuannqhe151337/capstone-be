package com.example.capstone_project.service.impl;

import com.example.capstone_project.entity.AccessTokenClaim;
import com.example.capstone_project.entity.FinancialPlan;
import com.example.capstone_project.entity.FinancialPlanExpense;
import com.example.capstone_project.repository.FinancialPlanExpenseRepository;
import com.example.capstone_project.repository.FinancialPlanRepository;
import com.example.capstone_project.service.FinancialPlanService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Service
@AllArgsConstructor
public class FinancialPlanServiceImpl implements FinancialPlanService {
    FinancialPlanRepository planRepository;
    FinancialPlanExpenseRepository expenseRepository;
    @Override
    @Transactional
    public void creatPlan(FinancialPlan plan, List<FinancialPlanExpense> expenseList, AccessTokenClaim tokenClaim) {

        expenseRepository.saveListExpenses(expenseList);


    }
}
