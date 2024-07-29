package com.example.capstone_project.repository;

import com.example.capstone_project.entity.AnnualReport;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CustomAnnualReportRepository {
    List<AnnualReport> getListAnnualReportPaging(Pageable pageable);
}
