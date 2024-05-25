package com.example.capstone_project.repository;

import com.example.capstone_project.entity.FinancialReport;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FinancialReportRepository extends JpaRepository<FinancialReport, Long> {
}
