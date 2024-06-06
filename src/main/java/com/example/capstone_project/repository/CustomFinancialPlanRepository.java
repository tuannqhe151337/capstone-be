package com.example.capstone_project.repository;

import com.example.capstone_project.entity.FinancialPlan;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CustomFinancialPlanRepository {
    List<FinancialPlan> getPlanWithPagination(String query, Pageable pageable);
}
