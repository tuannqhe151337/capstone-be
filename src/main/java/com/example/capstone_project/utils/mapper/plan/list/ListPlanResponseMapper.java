package com.example.capstone_project.utils.mapper.plan.list;

import com.example.capstone_project.controller.responses.plan.list.PlanResponse;
import com.example.capstone_project.entity.FinancialPlan;
import com.example.capstone_project.entity.FinancialPlanFile;
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
}
