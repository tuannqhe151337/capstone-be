package com.example.capstone_project.utils.mapper.plan.expenses;

import com.example.capstone_project.controller.responses.expense.list.ExpenseResponse;
import com.example.capstone_project.entity.FinancialPlanExpense;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
@Mapper(componentModel = "spring")
public interface PlanExpenseResponseMapper {
    @Mapping(source = "id", target = "expenseId")
    @Mapping(source = "costType.id", target = "costType.costTypeId")
    @Mapping(source = "status.id", target = "status.statusId")
    @Mapping(source = "project.id", target = "project.projectId")
    @Mapping(source = "supplier.id", target = "supplier.supplierId")
    @Mapping(source = "pic.id", target = "pic.picId")
    ExpenseResponse mapToExpenseResponseMapping(FinancialPlanExpense financialPlanExpense);
}
