package com.example.capstone_project.repository;

import com.example.capstone_project.entity.FinancialReportExpense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface FinancialReportExpenseRepository extends JpaRepository<FinancialReportExpense, Long>, CustomFinancialReportExpenseRepository {
    @Query(" SELECT count(distinct (expense.id)) FROM FinancialReportExpense expense " +
            " LEFT JOIN expense.financialReport report " +
            " LEFT JOIN expense.status status " +
            " LEFT JOIN expense.costType costType " +
            " WHERE report.id = :reportId AND " +
            " expense.name like %:query% AND " +
            " (:costTypeId IS NULL OR costType.id = :costTypeId) AND " +
            " (:statusId IS NULL OR status.id = :statusId) AND " +
            " (expense.isDelete = false OR expense.isDelete is null) ")
    long countDistinctListExpenseWithPaginate(String query, Long reportId, Integer statusId, Integer costTypeId);
}
