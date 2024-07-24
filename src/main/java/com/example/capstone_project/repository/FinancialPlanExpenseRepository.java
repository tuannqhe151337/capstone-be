package com.example.capstone_project.repository;

import com.example.capstone_project.entity.FinancialPlanExpense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FinancialPlanExpenseRepository extends JpaRepository<FinancialPlanExpense, Long>, CustomFinancialPlanExpenseRepository {
    @Query(value = "SELECT expenses FROM FinancialPlanExpense expenses " +
            " LEFT JOIN expenses.files files " +
            " LEFT JOIN files.file file " +
            " LEFT JOIN file.plan plan " +
            " WHERE plan.id = :planId AND " +
            " file.createdAt = (SELECT MAX(file_2.createdAt) FROM FinancialPlanFile file_2 WHERE file_2.plan.id = :planId) AND " +
            " file.isDelete = false AND expenses.isDelete = false ")
    List<FinancialPlanExpense> getListExpenseNewInLastVersion(Long planId);
}
