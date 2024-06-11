package com.example.capstone_project.repository;

import com.example.capstone_project.entity.FinancialPlanExpense;

import java.util.List;

public interface CustomFinancialPlanExpenseRepository {
    void saveListExpenses(List<FinancialPlanExpense> expenses);
}
