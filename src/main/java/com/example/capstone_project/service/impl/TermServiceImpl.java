package com.example.capstone_project.service.impl;


import com.example.capstone_project.repository.FinancialPlanRepository;
import com.example.capstone_project.entity.Term;
import com.example.capstone_project.repository.TermRepository;
import com.example.capstone_project.repository.redis.UserAuthorityRepository;
import com.example.capstone_project.service.TermService;
import com.example.capstone_project.utils.enums.AuthorityCode;
import com.example.capstone_project.utils.enums.TermCode;
import com.example.capstone_project.utils.helper.UserHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class TermServiceImpl implements TermService {
    private final TermRepository termRepository;
    private final UserAuthorityRepository userAuthorityRepository;

    @Override
    public long countDistinct(String query) {
        return termRepository.countDistinctListTermWhenCreatePlan(query, TermCode.CLOSED.getValue(), LocalDateTime.now());
    }

    @Override
    public List<Term> getListTermWhenCreatePlan(String query, Pageable pageable) {
        long userId = UserHelper.getUserId();

        if (userAuthorityRepository.get(userId).contains(AuthorityCode.IMPORT_PLAN.getValue())) {
            return termRepository.getListTermWhenCreatePlan(query, pageable);
        }

        return null;
    }
}
