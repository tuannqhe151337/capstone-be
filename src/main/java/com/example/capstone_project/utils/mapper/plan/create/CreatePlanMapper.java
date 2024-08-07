package com.example.capstone_project.utils.mapper.plan.create;

import com.example.capstone_project.controller.body.plan.create.ExpenseBody;
import com.example.capstone_project.controller.body.plan.create.NewPlanBody;
import com.example.capstone_project.entity.*;
import org.mapstruct.Mapper;

import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring")
public interface CreatePlanMapper {
    default FinancialPlan mapPlanBodyToPlanMapping(NewPlanBody newPlanBody, Long departmentId, Long userId, String termName) {
        // Get user detail


        FinancialPlan plan = new FinancialPlan();

        plan.setName(newPlanBody.getPlanName());

        plan.setTerm(Term.builder()
                .id(newPlanBody.getTermId())
                .build());


        plan.setDepartment(Department.builder().id(departmentId).build());

        List<FinancialPlanExpense> expenses = mapExpenseBodyToExpense(newPlanBody.getExpenses(), newPlanBody, termName);

        List<FinancialPlanFileExpense> expenseFile = new ArrayList<>();

        FinancialPlanFile file = FinancialPlanFile.builder()
                .plan(plan)
                .name(newPlanBody.getFileName())
                .user(User.builder().id(userId).build())
                .planFileExpenses(expenseFile)
                .build();

        expenses.forEach(expense -> {
            expenseFile.add(FinancialPlanFileExpense.builder()
                    .planExpense(expense)
                    .file(file)
                    .build());
        });

        List<FinancialPlanFile> files = new ArrayList<>();

        files.add(file);

        plan.setPlanFiles(files);

        return plan;
    }

    default List<FinancialPlanExpense> mapExpenseBodyToExpense(List<ExpenseBody> expenseBodies, NewPlanBody planBody, String termName) {
        List<FinancialPlanExpense> planExpenses = new ArrayList<>();
        for (int i = 0; i < expenseBodies.size(); i++) {
            ExpenseBody expenseBody = expenseBodies.get(i);
            planExpenses.add(FinancialPlanExpense.builder()
                    .planExpenseKey(termName + "_" + planBody.getPlanName() + "_v1" + "_" + (i + 1))
                    .name(expenseBody.getName())
                    .unitPrice(expenseBody.getUnitPrice())
                    .amount(expenseBody.getAmount())
                    .projectName(expenseBody.getProjectName())
                    .supplierName(expenseBody.getSupplierName())
                    .pic(expenseBody.getPic())
                    .note(expenseBody.getNotes())
                    .status(ExpenseStatus.builder()
                            .id(1L)
                            .build())
                    .costType(CostType.builder()
                            .id(expenseBody.getCostTypeId())
                            .build())
                    .build());
        }
        return planExpenses;
    }
}