package com.example.capstone_project.utils.mapper.plan.create;

import com.example.capstone_project.controller.body.plan.create.ExpenseBody;
import com.example.capstone_project.controller.body.plan.create.NewPlanBody;
import com.example.capstone_project.controller.responses.term.get.TermDetailResponse;
import com.example.capstone_project.controller.responses.term.get.TermStatusResponse;
import com.example.capstone_project.entity.*;
import com.example.capstone_project.utils.enums.ExpenseStatusCode;
import com.example.capstone_project.utils.enums.PlanStatusCode;
import com.example.capstone_project.utils.enums.TermCode;
import org.mapstruct.Mapper;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring")
public interface CreatePlanMapper {
    default FinancialPlan mapPlanBodyToPlanMapping(NewPlanBody newPlanBody, AccessTokenClaim tokenClaim) {
        FinancialPlan plan = new FinancialPlan();

        plan.setName(newPlanBody.getPlanName());

        plan.setTerm(Term.builder()
                .id(newPlanBody.getTermId())
                .build());


        plan.setDepartment(Department.builder().id(tokenClaim.getDepartmentId()).build());

        //
        plan.setStatus(PlanStatus.builder()
//                .id(1L)
                .code(PlanStatusCode.NEW).build());

        List<FinancialPlanExpense> expenses =mapExpenseBodyToExpense(newPlanBody.getExpenses(),newPlanBody);

        List<FinancialPlanFileExpense> expenseFile = new ArrayList<>();
        expenses.forEach(expense ->{
            expenseFile.add(FinancialPlanFileExpense.builder()
                    .planExpense(expense).build());
        });

        List<FinancialPlanFile> files = new ArrayList<>();

        files.add(FinancialPlanFile.builder()
                        .name(newPlanBody.getFileName())
                        .version("v1")
                        .user(User.builder().id(tokenClaim.getUserId()).build())
                        .planFileExpenses(expenseFile)
                        .build());

        plan.setPlanFiles(files);

        return plan;
    }

    default List<FinancialPlanExpense> mapExpenseBodyToExpense(List<ExpenseBody> expenseBodies, NewPlanBody planBody){
        List<FinancialPlanExpense> planExpenses = new ArrayList<>();
        for (int i = 0; i < expenseBodies.size(); i++) {
            ExpenseBody expenseBody = expenseBodies.get(i);
            planExpenses.add(FinancialPlanExpense.builder()
                    .planExpenseKey(planBody.getFileName()+"_"+(i+1))
                    .name(expenseBody.getName())
                    .unitPrice(expenseBody.getUnitPrice())
                    .amount(expenseBody.getAmount())
                    .projectName(expenseBody.getProjectName())
                    .supplierName(expenseBody.getSupplierName())
                    .pic(expenseBody.getPic())
                    .note(expenseBody.getNotes())
                    .status(ExpenseStatus.builder()
//                            .id(1L)
                            .code(ExpenseStatusCode.NEW)
                            .build())
                    .costType(CostType.builder()
                            .id(expenseBody.getCostTypeId())
                            .build())
                    .build());
        }
        return planExpenses;
    }
}