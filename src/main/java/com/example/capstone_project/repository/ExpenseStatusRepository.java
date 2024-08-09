package com.example.capstone_project.repository;

import com.example.capstone_project.entity.ExpenseStatus;
import com.example.capstone_project.utils.enums.ExpenseStatusCode;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExpenseStatusRepository extends JpaRepository<ExpenseStatus, Long> {
    ExpenseStatus findByCode(ExpenseStatusCode expenseStatusCode);

    ExpenseStatus getReferenceByCode(ExpenseStatusCode code);
}
