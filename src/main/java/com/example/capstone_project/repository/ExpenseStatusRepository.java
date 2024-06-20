package com.example.capstone_project.repository;

import com.example.capstone_project.entity.ExpenseStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExpenseStatusRepository extends JpaRepository<ExpenseStatus, Long> {
}
