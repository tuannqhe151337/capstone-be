package com.example.capstone_project.service.impl;

import com.example.capstone_project.entity.FinancialPlan;
import com.example.capstone_project.repository.FinancialPlanRepository;
import com.example.capstone_project.repository.redis.UserAuthorityRepository;
import com.example.capstone_project.service.FinancialPlanService;
import com.example.capstone_project.utils.enums.AuthorityCode;
import com.example.capstone_project.utils.helper.UserHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FinancialPlanServiceImpl implements FinancialPlanService {
    private final FinancialPlanRepository planRepository;
    private final UserAuthorityRepository userAuthorityRepository;
    @Override
    public long countDistinct(String query, Long termId, Long departmentId, Long statusId) {
        return planRepository.countDistinct(query, termId, departmentId, statusId);
    }

    @Override
    public List<FinancialPlan> getPlanWithPagination(String query, Pageable pageable) {
        long userId = UserHelper.getUserId();

        if (userAuthorityRepository.get(userId).contains(AuthorityCode.VIEW_TERM.getValue())) {
            return planRepository.getPlanWithPagination(query, pageable);
        }
        return null;
    }

}
