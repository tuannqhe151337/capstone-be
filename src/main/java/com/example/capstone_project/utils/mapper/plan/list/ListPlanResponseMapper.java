package com.example.capstone_project.utils.mapper.plan.list;

import com.example.capstone_project.controller.responses.plan.DepartmentResponse;
import com.example.capstone_project.controller.responses.plan.TermResponse;
import com.example.capstone_project.controller.responses.plan.list.PlanResponse;
import com.example.capstone_project.controller.responses.term.getPlans.PlanStatusResponse;
import com.example.capstone_project.controller.responses.plan.StatusResponse;
import com.example.capstone_project.entity.*;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;

@Mapper(componentModel = "spring")
public interface ListPlanResponseMapper {
    @Mapping(source = "id", target = "planId")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "term", target = "term")
    @Mapping(source = "status", target = "status")
    @Mapping(source = "department", target = "department")
    @Mapping(source = "planFiles", target = "version", qualifiedByName = "getNewestVersion")
    PlanResponse mapToPlanResponseMapper(FinancialPlan plan);

    @Named("getNewestVersion")
    default String getNewestVersion(List<FinancialPlanFile> planFiles) {
        if (planFiles == null || planFiles.isEmpty()) {
            return null;
        }

        return planFiles.stream()
                .min(Comparator.
                        comparing(planFile -> Math.abs(planFile.getCreatedAt().until(LocalDateTime.now(), ChronoUnit.SECONDS)))
                )
                .get().getVersion();
    }
    @Mapping(source = "id", target = "termId")
    TermResponse termToTermResponse(Term term);

    @Mapping(source = "id", target = "statusId")
    StatusResponse planStatusToStatusResponse(PlanStatus planStatus);

    @Mapping(source = "id", target = "departmentId")
    DepartmentResponse departmentToDepartmentResponse(Department department);
}
