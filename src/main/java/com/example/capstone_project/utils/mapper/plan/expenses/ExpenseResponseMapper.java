package com.example.capstone_project.utils.mapper.plan.expenses;

import com.example.capstone_project.controller.responses.expense.list.ExpenseResponse;
import com.example.capstone_project.entity.FinancialPlanExpense;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
@Mapper(componentModel = "spring")
public interface ExpenseResponseMapper {
    @Mapping(source = "id", target = "expenseId")
    @Mapping(source = "costType.id", target = "costType.costTypeId")
    @Mapping(source = "status.id", target = "status.statusId")
    ExpenseResponse mapToExpenseResponseMapping(FinancialPlanExpense financialPlanExpense);
}
