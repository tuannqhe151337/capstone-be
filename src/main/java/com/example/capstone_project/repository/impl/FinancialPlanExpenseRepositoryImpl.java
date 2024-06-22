package com.example.capstone_project.repository.impl;

import com.example.capstone_project.entity.FinancialPlanExpense;
import com.example.capstone_project.repository.CustomFinancialPlanExpenseRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Repository
public class FinancialPlanExpenseRepositoryImpl implements CustomFinancialPlanExpenseRepository {
    @PersistenceContext
    private EntityManager entityManager;
    @Override
    @Transactional
    public void saveListExpenses(List<FinancialPlanExpense> expenses) {
            for (FinancialPlanExpense expense : expenses) {
                entityManager.persist(expense);
            }
            entityManager.flush(); // Ensure all changes are synchronized with the database
            entityManager.clear(); // Clear the persistence context to avoid memory issues
    }
}
