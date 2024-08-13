package com.example.capstone_project.repository;

import com.example.capstone_project.entity.Report;
import com.example.capstone_project.repository.result.CostStatisticalByCurrencyResult;
import com.example.capstone_project.utils.enums.ExpenseStatusCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReportRepository extends JpaRepository<Report, Long> {
}
