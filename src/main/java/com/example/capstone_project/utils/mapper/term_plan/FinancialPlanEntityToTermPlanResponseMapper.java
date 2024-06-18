package com.example.capstone_project.utils.mapper.term_plan;


import com.example.capstone_project.controller.responses.term.getPlans.TermPlanDetailResponse;
import com.example.capstone_project.entity.FinancialPlan;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface FinancialPlanEntityToTermPlanResponseMapper {
    @Mapping(source = "plan.status.id", target = "planStatus.id")
    @Mapping(source = "plan.status.name", target = "planStatus.name")
    TermPlanDetailResponse mapPlanEntityToTermPlanResponse(FinancialPlan plan);

    List<TermPlanDetailResponse> mapPlanEntityToTermPlanResponseList(List<FinancialPlan> planList);

}
