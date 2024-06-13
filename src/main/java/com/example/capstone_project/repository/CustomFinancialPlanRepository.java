package com.example.capstone_project.repository;

import com.example.capstone_project.entity.FinancialPlan;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CustomFinancialPlanRepository {
    List<FinancialPlan> getPlanWithPagination(String query, Long termId, Long departmentId, Long statusId, Pageable pageable);
}
