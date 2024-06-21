package com.example.capstone_project.repository;

import com.example.capstone_project.entity.FinancialPlanExpense;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FinancialPlanExpenseRepository extends JpaRepository<FinancialPlanExpense, Long>, CustomFinancialPlanExpenseRepository {
}
