package com.example.capstone_project.utils.mapper.plan.list;

import com.example.capstone_project.controller.responses.plan.DepartmentResponse;
import com.example.capstone_project.controller.responses.plan.TermResponse;
import com.example.capstone_project.controller.responses.plan.list.PlanResponse;
import com.example.capstone_project.controller.responses.plan.StatusResponse;
import com.example.capstone_project.entity.*;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ListPlanResponseMapper {
    @Mapping(source = "id", target = "planId")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "term", target = "term")
    @Mapping(source = "department", target = "department")
    PlanResponse mapToPlanResponseMapper(FinancialPlan plan);

    @Mapping(source = "id", target = "termId")
    TermResponse termToTermResponse(Term term);

    @Mapping(source = "id", target = "departmentId")
    DepartmentResponse departmentToDepartmentResponse(Department department);
}
