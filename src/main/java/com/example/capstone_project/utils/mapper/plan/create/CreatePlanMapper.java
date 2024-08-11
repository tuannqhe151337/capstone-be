package com.example.capstone_project.utils.mapper.plan.create;

import com.example.capstone_project.controller.body.plan.create.ExpenseBody;
import com.example.capstone_project.controller.body.plan.create.NewPlanBody;
import com.example.capstone_project.entity.*;
import com.example.capstone_project.utils.enums.ExpenseStatusCode;
import org.mapstruct.Mapper;

import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring")
public interface CreatePlanMapper {
    default FinancialPlan mapPlanBodyToPlanMapping(NewPlanBody newPlanBody) {
        // Get user detail


        FinancialPlan plan = new FinancialPlan();

        plan.setName(newPlanBody.getPlanName());

        plan.setTerm(Term.builder()
                .id(newPlanBody.getTermId())
                .build());

        return plan;
    }

    default List<FinancialPlanExpense> mapExpenseBodyToExpense(List<ExpenseBody> expenseBodies) {
        List<FinancialPlanExpense> planExpenses = new ArrayList<>();
        for (int i = 0; i < expenseBodies.size(); i++) {
            ExpenseBody expenseBody = expenseBodies.get(i);
            planExpenses.add(FinancialPlanExpense.builder()
                    .name(expenseBody.getName())
                    .unitPrice(expenseBody.getUnitPrice())
                    .amount(expenseBody.getAmount())
                    .project(Project.builder()
                            .id(expenseBody.getProjectId()).build())
                    .supplier(Supplier.builder()
                            .id(expenseBody.getSupplierId()).build())
                    .pic(User.builder()
                            .id(expenseBody.getPicId()).build())
                    .note(expenseBody.getNotes())
                    .status(ExpenseStatus.builder()
                            .code(ExpenseStatusCode.NEW)
                            .build())
                    .costType(CostType.builder()
                            .id(expenseBody.getCostTypeId())
                            .build())
                    .currency(Currency.builder()
                            .id(expenseBody.getCurrencyId())
                            .build())
                    .build());
        }
        return planExpenses;
    }
}