package com.example.capstone_project.service;

import com.example.capstone_project.entity.FinancialPlanExpense;
import com.example.capstone_project.entity.FinancialReport;
import com.example.capstone_project.repository.result.ExpenseResult;
import com.example.capstone_project.repository.result.ReportDetailResult;
import com.example.capstone_project.repository.result.ReportExpenseResult;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface FinancialReportService {
    List<FinancialReport> getListReportPaginate(String query, Long termId, Long departmentId, Long statusId, Pageable pageable) throws Exception;

    long countDistinctListReportPaginate(String query, Long termId, Long departmentId, Long statusId) throws Exception;

//    long countDistinctListExpenseWithPaginate(String query, Long reportId, Integer statusId, Integer costTypeId);
//
//    ReportDetailResult getReportDetailByReportId(Long reportId) throws Exception;

    FinancialReport deleteReport(Long reportId);

//    byte[] getBodyFileExcelXLSX(Long reportId) throws Exception;

    String generateXLSXFileName(Long reportId);

//    byte[] getBodyFileExcelXLS(Long reportId) throws Exception;

    String generateXLSFileName(Long reportId);

    List<ReportExpenseResult> getListExpenseWithPaginate(Long reportId, String query, Integer departmentId, Integer statusId, Integer costTypeId, Pageable pageable);

    long countDistinctListExpenseWithPaginate(String query, Long reportId, Integer departmentId, Integer statusId, Integer costTypeId);
}
