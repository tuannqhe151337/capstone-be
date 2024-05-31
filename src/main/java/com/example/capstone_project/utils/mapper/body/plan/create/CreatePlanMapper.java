package com.example.capstone_project.utils.mapper.body.plan.create;

import com.example.capstone_project.controller.body.plan.create.ExpenseBody;
import com.example.capstone_project.controller.body.plan.create.NewPlanBody;
import com.example.capstone_project.entity.FinancialPlan;
import com.example.capstone_project.entity.FinancialPlanExpense;
import com.example.capstone_project.entity.FinancialPlanFile;
import com.example.capstone_project.entity.Term;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CreatePlanMapper {
    default FinancialPlan newPlanBodyToPlanMapping(NewPlanBody newPlanBody) {
        FinancialPlan plan = new FinancialPlan();
        plan.setTerm(Term.builder().id(newPlanBody.getTermId()).build());
        plan.setName(newPlanBody.getPlanName());
        plan.setFinancialPlanFiles(
                List.of(
                        FinancialPlanFile.builder()
                        .name(newPlanBody.getPlanName())
                                .financialPlanExpenses(listExpenseMapping(newPlanBody.getExpenses()))
                                .build()));
        return plan;
    }
    List<FinancialPlanExpense> listExpenseMapping(List<ExpenseBody> expenses);
}
