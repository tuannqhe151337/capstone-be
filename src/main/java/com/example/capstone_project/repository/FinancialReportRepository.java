package com.example.capstone_project.repository;

import com.example.capstone_project.entity.FinancialReport;
import com.example.capstone_project.repository.result.ReportDetailResult;
import com.example.capstone_project.repository.result.ExpenseResult;
import com.example.capstone_project.repository.result.FileNameResult;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FinancialReportRepository extends JpaRepository<FinancialReport, Long>, CustomFinancialReportRepository {
    @Query(value = " SELECT DISTINCT count(report.id) FROM FinancialReport report " +
            " WHERE report.name like %:query% AND " +
            " (:termId IS NULL OR report.term.id = :termId) AND " +
            " (:statusId IS NULL OR report.status.id = :statusId) AND " +
            " report.isDelete = false ")
    long countDistinctListReportPaginate(@Param("query") String query, @Param("termId") Long termId, @Param("statusId") Long statusId);

    @Query(" SELECT report.id AS reportId, report.name AS name, term.id AS termId, term.name AS termName, status.id AS statusId, status.name AS statusName, " +
            " report.createdAt AS createdAt, term.endDate AS endDate, term.reuploadStartDate AS reuploadStartDate, term.reuploadEndDate AS reuploadEndDate, term.finalEndTermDate AS finalEndTermDate FROM FinancialReport report " +
            " JOIN report.term term " +
            " JOIN report.status status " +
            " WHERE report.id = :reportId AND " +
            " report.isDelete = false OR report.isDelete is null ")
    ReportDetailResult getFinancialReportById(Long reportId);

    @Query(value = " SELECT term.name AS termName FROM FinancialReport report " +
            " JOIN report.term term " +
            " WHERE report.id = :reportId AND " +
            " report.isDelete = false AND term.isDelete = false ")
    FileNameResult generateFileName(Long reportId);

//    @Query(value = " SELECT expenses FROM FinancialReportExpense expenses " +
//            " JOIN expenses.financialReport report " +
//            " WHERE report.id = :reportId AND " +
//            " expenses.isDelete = false AND report.isDelete = false ")
//    List<ExpenseResult> getListExpenseByReportId(Long reportId);

//    @Query(value = " SELECT report.department.id FROM FinancialReport report " +
//            " WHERE report.id = :reportId AND " +
//            " report.isDelete = false ")
//    long getDepartmentId(Long reportId);
}