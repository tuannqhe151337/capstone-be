package com.example.capstone_project.utils.mapper.plan.version;

import com.example.capstone_project.controller.responses.plan.version.VersionResponse;
import com.example.capstone_project.entity.FinancialPlanFile;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring")
public interface PlanListVersionResponseMapper {
    @Mapping(source = "createdAt", target = "publishedDate")
    @Mapping(source = "user.id", target = "uploadedBy.userId")
    @Mapping(source = "user.username", target = "uploadedBy.username")
    VersionResponse mapToPlanVersionResponseMapper(FinancialPlanFile financialPlanFile);
}
