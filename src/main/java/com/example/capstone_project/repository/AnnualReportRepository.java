package com.example.capstone_project.repository;

import com.example.capstone_project.entity.AnnualReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AnnualReportRepository extends JpaRepository<AnnualReport, Long>, CustomAnnualReportRepository {
    @Query(value = "SELECT count( distinct(annualReport)) FROM AnnualReport annualReport " +
            " WHERE annualReport.year = :year AND " +
            " annualReport.isDelete = false ")
    long countDistinctListAnnualReportPaging(String year);

    @Query(value = " SELECT count (distinct (report.id)) FROM Report report " +
            " JOIN report.annualReport annualReport " +
            " JOIN report.costType costType " +
            " JOIN report.department department " +
            " WHERE annualReport.id = :annualReportId AND " +
            " (:departmentId IS NULL OR report.department.id = :departmentId) AND " +
            " (:costTypeId IS NULL OR report.costType = :costTypeId) AND " +
            " report.isDelete = false OR report.isDelete is null ")
    long countDistinctListExpenseWithPaginate(Long annualReportId, Long costTypeId, Long departmentId);
}
