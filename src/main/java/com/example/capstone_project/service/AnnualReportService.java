package com.example.capstone_project.service;

import com.example.capstone_project.entity.AnnualReport;
import com.example.capstone_project.entity.Report;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AnnualReportService {
    List<AnnualReport> getListAnnualReportPaging(Pageable pageable, String year);

    long countDistinctListAnnualReportPaging(String year);

    List<Report> getListExpenseWithPaginate(Long annualReportId, Long costTypeId, Long departmentId, Pageable pageable);

    long countDistinctListExpenseWithPaginate(Long annualReportId, Long costTypeId, Long departmentId);
}
