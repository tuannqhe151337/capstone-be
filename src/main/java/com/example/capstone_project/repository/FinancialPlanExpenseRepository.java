package com.example.capstone_project.repository;

import com.example.capstone_project.entity.FinancialPlanExpense;
import com.example.capstone_project.repository.result.ExpenseResult;
import com.example.capstone_project.utils.enums.TermCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface FinancialPlanExpenseRepository extends JpaRepository<FinancialPlanExpense, Long>, CustomFinancialPlanExpenseRepository {
    @Query(" SELECT count(distinct expense.id) FROM FinancialPlanExpense expense " +
            " JOIN expense.files fileExpense " +
            " JOIN fileExpense.file file " +
            " JOIN file.plan plan " +
            " JOIN plan.term term " +
            " JOIN term.status status " +
            " WHERE expense.id IN (:listExpenses) AND " +
            " status.code = :inProgress AND " +
            " term.planDueDate >= :now AND " +
            " plan.id = :planId AND " +
            " file.createdAt = (SELECT MAX(file_2.createdAt) FROM FinancialPlanFile file_2 WHERE file_2.plan.id = :planId) AND " +
            " expense.isDelete = false ")
    long countTotalExpenseInPlanLastVersion(Long planId, List<Long> listExpenses, TermCode inProgress, LocalDateTime now);

    @Query(" SELECT count(distinct (expense.id)) FROM FinancialPlanExpense expense " +
            " LEFT JOIN expense.files files " +
            " LEFT JOIN files.file file " +
            " LEFT JOIN file.plan plan " +
            " LEFT JOIN expense.status status " +
            " LEFT JOIN expense.costType costType " +
            " WHERE plan.id = :planId AND " +
            " file.createdAt = (SELECT MAX(file_2.createdAt) FROM FinancialPlanFile file_2 WHERE file_2.plan.id = :planId) AND " +
            " expense.name like %:query% AND " +
            " (:costTypeId IS NULL OR costType.id = :costTypeId) AND " +
            " (:statusId IS NULL OR status.id = :statusId) AND " +
            " (expense.isDelete = false OR expense.isDelete is null) ")
    long countDistinctListExpenseWithPaginate(@Param("query") String query, @Param("planId") Long planId, @Param("statusId") Long statusId, @Param("costTypeId") Long costTypeId);

    @Query(value = " SELECT expense.id AS expenseId, expense.planExpenseKey AS expenseCode, expense.status.code AS statusCode FROM FinancialPlanExpense expense " +
            " JOIN expense.files files " +
            " JOIN files.file file " +
            " JOIN file.plan plan " +
            " WHERE plan.id = :planId AND " +
            " file.createdAt = (SELECT MAX(file_2.createdAt) FROM FinancialPlanFile file_2 WHERE file_2.plan.id = :planId) AND " +
            " expense.isDelete = false ")
    List<ExpenseResult> getListExpenseByPlanId(Long planId);

    @Query(value = " SELECT expense.planExpenseKey FROM FinancialPlanExpense expense " +
            " JOIN expense.files files " +
            " JOIN files.file file " +
            " JOIN file.plan plan " +
            " WHERE plan.id = :planId AND " +
            " file.createdAt = (SELECT MAX(file_2.createdAt) FROM FinancialPlanFile file_2 WHERE file_2.plan.id = :planId) AND " +
            " expense.isDelete = false " +
            " ORDER BY expense.id DESC LIMIT 1")
    String getLastExpenseCode(Long planId);

    @Query(" SELECT expense FROM FinancialPlanExpense expense " +
            " JOIN expense.files fileExpense " +
            " JOIN fileExpense.file file " +
            " JOIN file.plan plan " +
            " JOIN plan.term term " +
            " JOIN term.status status " +
            " WHERE plan.id = :planId AND " +
            " status.code = :inProgress AND " +
            " term.planDueDate >= :now AND " +
            " file.createdAt = (SELECT MAX(file_2.createdAt) FROM FinancialPlanFile file_2 WHERE file_2.plan.id = :planId) AND " +
            " expense.isDelete = false ")
    List<FinancialPlanExpense> getListExpenseByPlanId(Long planId, TermCode inProgress, LocalDateTime now);

    @Query(value = "SELECT expenses FROM FinancialPlanExpense expenses " +
            " LEFT JOIN expenses.files files " +
            " LEFT JOIN files.file file " +
            " LEFT JOIN file.plan plan " +
            " WHERE plan.id = :planId AND " +
            " file.createdAt = (SELECT MAX(file_2.createdAt) FROM FinancialPlanFile file_2 WHERE file_2.plan.id = :planId) AND " +
            " file.isDelete = false AND expenses.isDelete = false ")
    List<FinancialPlanExpense> getListExpenseNewInLastVersion(Long planId);
}
