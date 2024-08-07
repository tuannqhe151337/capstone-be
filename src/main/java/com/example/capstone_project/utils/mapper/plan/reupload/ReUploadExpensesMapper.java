package com.example.capstone_project.utils.mapper.plan.reupload;

import com.example.capstone_project.controller.body.plan.reupload.ReUploadExpenseBody;
import com.example.capstone_project.entity.*;
import com.example.capstone_project.repository.result.PlanVersionResult;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring")
public interface ReUploadExpensesMapper {

    @Mapping(source = "expenseCode", target = "planExpenseKey")
    @Mapping(source = "expenseName", target = "name")
    @Mapping(source = "costTypeId", target = "costType.id")
    @Mapping(source = "unitPrice", target = "unitPrice")
    @Mapping(source = "amount", target = "amount")
    @Mapping(source = "projectId", target = "project.id")
    @Mapping(source = "supplierId", target = "supplier.id")
    @Mapping(source = "picId", target = "pic.id")
    @Mapping(source = "notes", target = "note")
    @Mapping(constant = "1L", target = "status.id")
    FinancialPlanExpense mapUpdateExpenseToPlanExpense(ReUploadExpenseBody reUploadExpenseBody);

    default FinancialPlanExpense newExpenseToPlanExpense(ReUploadExpenseBody reUploadExpenseBody, StringBuilder prefixExpenseKey, Integer version, Integer lastIndexCode) {
        return FinancialPlanExpense.builder()
                .planExpenseKey(prefixExpenseKey + "v" + version + "_" + lastIndexCode)
                .name(reUploadExpenseBody.getExpenseName())
                .costType(CostType.builder()
                        .id(reUploadExpenseBody.getCostTypeId())
                        .build())
                .status(ExpenseStatus.builder().id(1L).build())
                .unitPrice(reUploadExpenseBody.getUnitPrice())
                .amount(reUploadExpenseBody.getAmount())
                .project(Project.builder()
                        .id(reUploadExpenseBody.getProjectId())
                        .build())
                .supplier(Supplier.builder()
                        .id(reUploadExpenseBody.getSupplierId())
                        .build())
                .pic(User.builder()
                        .id(reUploadExpenseBody.getPicId())
                        .build())
                .note(reUploadExpenseBody.getNotes()
                ).build();
    }


    default FinancialPlan mapToPlanMapping(FinancialPlan plan, Long userId, PlanVersionResult planVersionResult, List<FinancialPlanExpense> expenses) {
        // Get user detail

        List<FinancialPlanFileExpense> expenseFile = new ArrayList<>();

        FinancialPlanFile file = FinancialPlanFile.builder()
                .plan(plan)
                .name(planVersionResult.getTermName() + "_" + planVersionResult.getDepartmentName() + "_v" + planVersionResult.getVersion())
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
}
