package com.example.capstone_project.utils.mapper.annual;

import com.example.capstone_project.controller.responses.annualReport.expenses.AnnualReportExpenseResponse;
import com.example.capstone_project.entity.MonthlyReportSummary;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AnnualReportExpenseMapper {
    @Mapping(source = "id", target = "expenseId")
    @Mapping(source = "department.id", target = "department.departmentId")
    @Mapping(source = "department.name", target = "department.name")
    @Mapping(source = "totalExpense", target = "totalExpenses")
    @Mapping(source = "biggestExpenditure", target = "biggestExpenditure")
    @Mapping(source = "costType.id", target = "costType.costTypeId")
    @Mapping(source = "costType.name", target = "costType.name")
    AnnualReportExpenseResponse mapToAnnualReportExpenseResponseMapping(MonthlyReportSummary monthlyReportSummary);
}
