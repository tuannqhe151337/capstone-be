package com.example.capstone_project.repository;

import com.example.capstone_project.entity.FinancialPlanFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FinancialPlanFileRepository extends JpaRepository<FinancialPlanFile, Long> {
}
