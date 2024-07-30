package com.example.capstone_project.service;

import com.example.capstone_project.entity.AnnualReport;
import com.example.capstone_project.entity.Report;
import com.example.capstone_project.repository.result.CostTypeDiagramResult;
import org.springframework.data.domain.Pageable;

import java.io.IOException;
import java.util.List;

public interface AnnualReportService {
    List<AnnualReport> getListAnnualReportPaging(Pageable pageable, String year);

    long countDistinctListAnnualReportPaging(String year);

    List<Report> getListExpenseWithPaginate(Long annualReportId, Long costTypeId, Long departmentId, Pageable pageable);

    long countDistinctListExpenseWithPaginate(Long annualReportId, Long costTypeId, Long departmentId);

    List<CostTypeDiagramResult> getAnnualReportCostTypeDiagram(Long annualReportId);

    AnnualReport getAnnualReportDetail(Long annualReportId);

    byte[] getBodyFileExcelXLSX(Long annualReportId) throws IOException;

    String generateXLSXFileName(Long annualReportId);

    byte[] getBodyFileExcelXLS(Long annualReportId) throws IOException;

    String generateXLSFileName(Long annualReportId);
}
