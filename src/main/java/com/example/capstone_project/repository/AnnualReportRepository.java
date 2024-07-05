package com.example.capstone_project.repository;

import com.example.capstone_project.entity.AnnualReport;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AnnualReportRepository extends JpaRepository<AnnualReport, Long>, CustomAnnualReportRepository {
    @Query(value = "SELECT count( distinct(annualReport)) FROM AnnualReport annualReport " +
            " WHERE annualReport.year = :year AND " +
            " annualReport.isDelete = false ")
    long countDistinctListAnnualReportPaging(String year);
}
