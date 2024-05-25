package com.example.capstone_project.utils.mapper;

import com.example.capstone_project.controller.responses.FinancialPlanResponse;
import com.example.capstone_project.entity.FinancialPlan;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface FinancialPlanMapper {
    FinancialPlanResponse mapToFinancialPlanResponse(FinancialPlan financialPlan);
    FinancialPlan mapToFinancialPlan(FinancialPlanResponse financialPlanResponse);
}
