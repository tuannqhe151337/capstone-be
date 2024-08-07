package com.example.capstone_project.repository;

import com.example.capstone_project.entity.ReportStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FinancialStatusRepository extends JpaRepository<ReportStatus, Long> {
}
