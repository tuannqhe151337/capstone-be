package com.example.capstone_project.utils.mapper.plan.detail;

import com.example.capstone_project.controller.responses.plan.detail.PlanDetailResponse;
import com.example.capstone_project.repository.result.PlanDetailResult;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PlanDetailMapper {
    @Mapping(expression = "java(planDetailResult.getPlanId())", target = "id")
    @Mapping(expression = "java(planDetailResult.getName())", target = "name")
    @Mapping(expression = "java(planDetailResult.getBiggestExpenditure())", target = "biggestExpenditure")
    @Mapping(expression = "java(planDetailResult.getTotalPlan())", target = "totalPlan")
    @Mapping(expression = "java(planDetailResult.getTermId())", target = "term.termId")
    @Mapping(expression = "java(planDetailResult.getTermName())", target = "term.name")
    @Mapping(expression = "java(planDetailResult.getStatusId())", target = "status.statusId")
    @Mapping(expression = "java(planDetailResult.getStatusName())", target = "status.name")
    @Mapping(expression = "java(planDetailResult.getStatusCode())", target = "status.code")
    @Mapping(expression = "java(planDetailResult.getPlanDueDate())", target = "planDueDate")
    @Mapping(expression = "java(planDetailResult.getCreatedAt())", target = "createdAt")
    @Mapping(expression = "java(planDetailResult.getDepartmentId())", target = "department.departmentId")
    @Mapping(expression = "java(planDetailResult.getDepartmentName())", target = "department.name")
    @Mapping(expression = "java(planDetailResult.getUserId())", target = "user.userId")
    @Mapping(expression = "java(planDetailResult.getUsername())", target = "user.username")
    PlanDetailResponse mapToPlanDetailResponseMapping(PlanDetailResult planDetailResult);

}
