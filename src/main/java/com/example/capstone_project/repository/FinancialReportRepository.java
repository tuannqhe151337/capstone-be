package com.example.capstone_project.repository;

import com.example.capstone_project.entity.FinancialReport;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FinancialReportRepository extends JpaRepository<FinancialReport, Long>, CustomFinancialReportRepository {
    @Query(value = "SELECT DISTINCT count(report.id) FROM FinancialReport report " +
            " WHERE report.name like %:query% AND " +
            " (:termId IS NULL OR report.term.id = :termId) AND " +
            " (:departmentId IS NULL OR report.department.id = :departmentId) AND " +
            " (:statusId IS NULL OR report.status.id = :statusId) AND " +
            " report.isDelete = false ")
    long countDistinctListReportPaginate(@Param("query") String query, @Param("termId") Long termId, @Param("departmentId") Long departmentId, @Param("statusId") Long statusId);
}
