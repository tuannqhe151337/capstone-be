package com.example.capstone_project.service.impl;


import com.example.capstone_project.entity.UserDetail;
import com.example.capstone_project.repository.FinancialPlanRepository;
import com.example.capstone_project.entity.Term;
import com.example.capstone_project.repository.TermRepository;
import com.example.capstone_project.repository.UserRepository;
import com.example.capstone_project.repository.redis.UserAuthorityRepository;
import com.example.capstone_project.repository.redis.UserDetailRepository;
import com.example.capstone_project.service.TermService;
import com.example.capstone_project.utils.enums.AuthorityCode;
import com.example.capstone_project.utils.enums.TermCode;
import com.example.capstone_project.utils.exception.ResourceNotFoundException;
import com.example.capstone_project.utils.exception.UnauthorizedException;
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
    private final UserDetailRepository userDetailRepository;
    private final UserAuthorityRepository userAuthorityRepository;

    @Override
    public long countDistinct(String query) throws Exception {
        // Get user detail
        UserDetail userDetail = userDetailRepository.get(UserHelper.getUserId());

        return termRepository.countDistinctListTermWhenCreatePlan(query, TermCode.CLOSED.getValue(), LocalDateTime.now(), userDetail.getDepartmentId());
    }

    @Override
    public List<Term> getListTermWhenCreatePlan(String query, Pageable pageable) throws Exception {
        // Get userId
        long userId = UserHelper.getUserId();
        // Get user detail
        UserDetail userDetail = userDetailRepository.get(userId);

        if (userAuthorityRepository.get(userId).contains(AuthorityCode.IMPORT_PLAN.getValue())) {
            return termRepository.getListTermWhenCreatePlan(query, pageable, userDetail.getDepartmentId());
        }

        return null;
    }

    @Override
    public void deleteTerm(Long id) throws Exception {
        long userId = UserHelper.getUserId();
        if (!userAuthorityRepository.get(userId).contains(AuthorityCode.DELETE_TERM.getValue())) {
            throw new UnauthorizedException("Unauthorized");
        }
        Term currentterm = termRepository.findById(id).
                orElseThrow(() -> new ResourceNotFoundException("Term not exist with id: " + id));
        currentterm.setDelete(true);
        termRepository.save(currentterm);

    }
}
