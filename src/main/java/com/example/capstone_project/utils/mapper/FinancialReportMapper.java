package com.example.capstone_project.utils.mapper;

import com.example.capstone_project.controller.responses.FinancialReportResponse;
import com.example.capstone_project.entity.FinancialReport;
import com.example.capstone_project.entity.FinancialReportExpense;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface FinancialReportMapper {
    FinancialReportResponse mapToFinancialReportResponse(FinancialReport financialReport);
    FinancialReport mapToFinancialReport(FinancialReportExpense financialReportExpense);
}
