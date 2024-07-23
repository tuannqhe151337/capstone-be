package com.example.capstone_project.repository;

import com.example.capstone_project.entity.AnnualReport;
import com.example.capstone_project.entity.Report;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CustomAnnualReportRepository {

    List<Report> getListExpenseWithPaginate(Long annualReportId, Long costTypeId, Long departmentId, Pageable pageable);

    List<AnnualReport> getListAnnualReportPaging(Pageable pageable, String year);
}
