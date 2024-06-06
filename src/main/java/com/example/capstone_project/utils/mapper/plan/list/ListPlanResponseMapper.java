package com.example.capstone_project.utils.mapper.plan.list;

import com.example.capstone_project.controller.responses.plan.list.PlanResponse;
import com.example.capstone_project.entity.FinancialPlan;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ListPlanResponseMapper {
    @Mapping(source = "id", target = "userId")
    PlanResponse mapToPlanResponseMapper(FinancialPlan plan);
}
