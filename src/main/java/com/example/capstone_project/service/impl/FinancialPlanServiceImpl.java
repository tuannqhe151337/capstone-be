package com.example.capstone_project.service.impl;

import com.example.capstone_project.entity.AccessTokenClaim;
import com.example.capstone_project.entity.FinancialPlan;
import com.example.capstone_project.entity.FinancialPlanExpense;
import com.example.capstone_project.repository.FinancialPlanExpenseRepository;
import com.example.capstone_project.repository.FinancialPlanRepository;
import com.example.capstone_project.repository.redis.UserAuthorityRepository;
import com.example.capstone_project.service.FinancialPlanService;
import com.example.capstone_project.utils.enums.AuthorityCode;
import com.example.capstone_project.utils.helper.UserHelper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Service
@AllArgsConstructor
public class FinancialPlanServiceImpl implements FinancialPlanService {
    FinancialPlanRepository planRepository;
    FinancialPlanExpenseRepository expenseRepository;
    UserAuthorityRepository userAuthorityRepository;
    @Override
    @Transactional
    public void creatPlan(FinancialPlan plan, List<FinancialPlanExpense> expenseList, AccessTokenClaim tokenClaim) {

        if (userAuthorityRepository.get(tokenClaim.getUserId()).contains(AuthorityCode.VIEW_LIST_USERS.getValue())){
            planRepository.save(plan);
        }

//        expenseRepository.saveListExpenses(expenseList);
    }
}
