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
            " (:departmentId IS NULL OR report.department.id = :departmentId) AND " +
            " (:statusId IS NULL OR report.status.id = :statusId) AND " +
            " report.isDelete = false ")
    long countDistinctListReportPaginate(@Param("query") String query, @Param("termId") Long termId, @Param("departmentId") Long departmentId, @Param("statusId") Long statusId);

    @Query(" SELECT report.id AS reportId, report.name AS name, MAX(expense.unitPrice * expense.amount) AS biggestExpenditure, " +
            " SUM(expense.unitPrice * expense.amount) AS totalCost, term.id AS termId, term.name AS termName, " +
            " term.planDueDate AS planDueDate, report.createdAt AS createdAt, department.id AS departmentId, department.name AS departmentName, " +
            " user.id AS userId , user.username AS username" +
            " FROM FinancialReport report " +
            " JOIN report.term term " +
            " JOIN report.department department " +
            " JOIN report.reportExpenses expense " +
            " JOIN report.user user" +
            " WHERE report.id = :reportId AND " +
            " report.isDelete = false AND expense.isDelete = false " +
            " GROUP BY report.id, report.name, term.id, term.name, term.planDueDate, " +
            " report.createdAt, department.id, department.name, user.id, user.username ")
    ReportDetailResult getFinancialReportById(Long reportId);

    @Query(value = " SELECT term.name AS termName, department.name AS departmentCode FROM FinancialReport report " +
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
