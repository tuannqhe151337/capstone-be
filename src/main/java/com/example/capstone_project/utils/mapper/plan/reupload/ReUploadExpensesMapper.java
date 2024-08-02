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
    @Mapping(source = "projectName", target = "projectName")
    @Mapping(source = "supplierName", target = "supplierName")
    @Mapping(source = "pic", target = "pic")
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
                .projectName(reUploadExpenseBody.getProjectName())
                .supplierName(reUploadExpenseBody.getSupplierName())
                .pic(reUploadExpenseBody.getPic())
                .note(reUploadExpenseBody.getNotes()
                ).build();
    }


    default FinancialPlan mapToPlanMapping(Long planId, Long userId, PlanVersionResult planVersionResult, List<FinancialPlanExpense> expenses) {
        // Get user detail
        FinancialPlan plan = new FinancialPlan();

        plan.setId(planId);

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
