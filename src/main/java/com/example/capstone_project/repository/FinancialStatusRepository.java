package com.example.capstone_project.repository;

import com.example.capstone_project.entity.PlanStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FinancialStatusRepository extends JpaRepository<PlanStatus, Long> {
}
