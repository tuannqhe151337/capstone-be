package com.example.capstone_project.service;

import com.example.capstone_project.entity.AccessTokenClaim;
import com.example.capstone_project.entity.FinancialPlan;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface FinancialPlanService {
    long countDistinct(String query, Long termId, Long departmentId, Long statusId);

    List<FinancialPlan> getPlanWithPagination(String query, Long termId, Long departmentId, Long statusId, Integer page, Integer size, String sortBy, String sortType, AccessTokenClaim tokenClaim);
}
