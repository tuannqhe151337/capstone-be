package com.example.capstone_project.repository;

import com.example.capstone_project.entity.FinancialPlanExpense;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CustomFinancialPlanExpenseRepository {
    void saveListExpenses(List<FinancialPlanExpense> expenses);

    List<FinancialPlanExpense> getListExpenseWithPaginate(Long planId, String query, Long statusId, Long costTypeId, Pageable pageable);
}
