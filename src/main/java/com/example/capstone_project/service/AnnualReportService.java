package com.example.capstone_project.service;

import com.example.capstone_project.entity.AnnualReport;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AnnualReportService {
    List<AnnualReport> getListAnnualReportPaging(Pageable pageable);

    long countDistinctListAnnualReportPaging();
}
