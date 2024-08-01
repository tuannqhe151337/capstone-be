package com.example.capstone_project.repository;

import com.example.capstone_project.entity.FinancialPlan;
import com.example.capstone_project.repository.result.VersionResult;
import com.example.capstone_project.utils.enums.PlanStatusCode;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CustomFinancialPlanRepository {

    List<VersionResult> getListVersionWithPaginate(Long planId, Pageable pageable);

    List<FinancialPlan> getPlanWithPagination(String query, Long termId, Long departmentId, Pageable pageable);
}