package com.example.capstone_project.repository;

import com.example.capstone_project.entity.FinancialPlan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FinancialPlanRepository extends JpaRepository<FinancialPlan, Long> {

    List<FinancialPlan> findFinancialPlansByTermId(Long termId);
}
