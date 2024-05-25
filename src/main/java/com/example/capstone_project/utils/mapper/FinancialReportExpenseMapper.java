package com.example.capstone_project.utils.mapper;

import com.example.capstone_project.controller.responses.FinancialReportExpenseResponse;
import com.example.capstone_project.entity.FinancialReportExpense;
import org.mapstruct.Mapper;
@Mapper(componentModel = "spring")
public interface FinancialReportExpenseMapper {
    FinancialReportExpenseResponse mapToFinancialReportExpenseResponse(FinancialReportExpense financialReportExpense);
    FinancialReportExpense mapToFinancialReportExpense(FinancialReportExpenseResponse financialReportExpenseResponse);
}
