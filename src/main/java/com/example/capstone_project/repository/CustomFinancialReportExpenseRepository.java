package com.example.capstone_project.repository;

import com.example.capstone_project.entity.FinancialReportExpense;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CustomFinancialReportExpenseRepository {
    List<FinancialReportExpense> getListExpenseWithPaginate(Long reportId, String query, Integer statusId, Integer costTypeId, Pageable pageable);
}
