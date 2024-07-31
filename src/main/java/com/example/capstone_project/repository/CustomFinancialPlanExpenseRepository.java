package com.example.capstone_project.repository;

import com.example.capstone_project.entity.FinancialPlanExpense;
import com.example.capstone_project.repository.result.ReportExpenseResult;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CustomFinancialPlanExpenseRepository {
    void saveListExpenses(List<FinancialPlanExpense> expenses);

    List<FinancialPlanExpense> getListExpenseWithPaginate(Long planId, String query, Long statusId, Long costTypeId, Pageable pageable);

    List<ReportExpenseResult> getListExpenseForReport(Long reportId, String query, Integer departmentId, Integer statusId, Integer costTypeId, Pageable pageable);
}