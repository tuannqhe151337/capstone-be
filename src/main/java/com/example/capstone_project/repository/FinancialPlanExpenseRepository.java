package com.example.capstone_project.repository;

import com.example.capstone_project.entity.FinancialPlanExpense;
import com.example.capstone_project.repository.result.ExpenseResult;
import com.example.capstone_project.utils.enums.ExpenseStatusCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FinancialPlanExpenseRepository extends JpaRepository<FinancialPlanExpense, Long>, CustomFinancialPlanExpenseRepository {

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
}
