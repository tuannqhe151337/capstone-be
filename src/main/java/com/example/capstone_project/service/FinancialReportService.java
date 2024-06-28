package com.example.capstone_project.service;

import com.example.capstone_project.entity.FinancialReport;
import com.example.capstone_project.entity.FinancialReportExpense;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface FinancialReportService {
    List<FinancialReport> getListReportPaginate(String query, Long termId, Long departmentId, Long statusId, Pageable pageable) throws Exception;

    long countDistinctListReportPaginate(String query, Long termId, Long departmentId, Long statusId) throws Exception;

    List<FinancialReportExpense> getListExpenseWithPaginate(Long reportId, String query, Integer statusId, Integer costTypeId, Pageable pageable) throws Exception;

    long countDistinctListExpenseWithPaginate(String query, Long reportId, Integer statusId, Integer costTypeId);
}
