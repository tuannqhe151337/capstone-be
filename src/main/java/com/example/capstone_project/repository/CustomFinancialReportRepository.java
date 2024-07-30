package com.example.capstone_project.repository;

import com.example.capstone_project.entity.FinancialReport;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CustomFinancialReportRepository {
    List<FinancialReport> getReportWithPagination(String query, Long termId, Long departmentId, Long statusId, Pageable pageable);

}