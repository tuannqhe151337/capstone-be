package com.example.capstone_project.utils.mapper.report.expenses;

import com.example.capstone_project.controller.responses.expense.list.ExpenseResponse;
import com.example.capstone_project.entity.FinancialReportExpense;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
@Mapper(componentModel = "spring")
public interface ExpenseResponseMapper {
    @Mapping(source = "id", target = "expenseId")
    @Mapping(source = "costType.id", target = "costType.costTypeId")
    @Mapping(source = "status.id", target = "status.statusId")
    @Mapping(source = "status.code", target = "status.code")
    ExpenseResponse mapToExpenseResponseMapping(FinancialReportExpense financialPlanExpense);
}