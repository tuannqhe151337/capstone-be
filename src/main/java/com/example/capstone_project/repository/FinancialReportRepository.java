package com.example.capstone_project.repository;

import com.example.capstone_project.entity.FinancialReport;
import com.example.capstone_project.repository.result.ExpenseResult;
import com.example.capstone_project.repository.result.FileNameResult;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FinancialReportRepository extends JpaRepository<FinancialReport, Long>, CustomFinancialReportRepository {
    @Query(value = " SELECT DISTINCT count(report.id) FROM FinancialReport report " +
            " WHERE report.name like %:query% AND " +
            " (:termId IS NULL OR report.term.id = :termId) AND " +
            " (:departmentId IS NULL OR report.department.id = :departmentId) AND " +
            " (:statusId IS NULL OR report.status.id = :statusId) AND " +
            " report.isDelete = false ")
    long countDistinctListReportPaginate(@Param("query") String query, @Param("termId") Long termId, @Param("departmentId") Long departmentId, @Param("statusId") Long statusId);
    @Query(value = " SELECT term.name AS termName, department.code AS departmentCode FROM FinancialReport report " +
            " JOIN report.department department " +
            " JOIN report.term term " +
            " WHERE report.id = :reportId AND " +
            " report.isDelete = false AND department.isDelete = false AND term.isDelete = false ")
    FileNameResult generateFileName(Long reportId);
    @Query(value = " SELECT expenses FROM FinancialReportExpense expenses " +
            " JOIN expenses.financialReport report " +
            " WHERE report.id = :reportId AND " +
            " expenses.isDelete = false AND report.isDelete = false ")
    List<ExpenseResult> getListExpenseByReportId(Long reportId);
    @Query(value = " SELECT report.department.id FROM FinancialReport report " +
            " WHERE report.id = :reportId AND " +
            " report.isDelete = false ")
    long getDepartmentId(Long reportId);
}
