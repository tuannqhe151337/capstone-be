package com.example.capstone_project.utils.mapper.plan.version;

import com.example.capstone_project.controller.responses.plan.version.VersionResponse;
import com.example.capstone_project.entity.FinancialPlanFile;
import com.example.capstone_project.repository.result.PlanVersionResult;
import com.example.capstone_project.repository.result.VersionResult;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring")
public interface PlanListVersionResponseMapper {
    @Mapping(source = "fileId", target = "planFileId")
    @Mapping(source = "version", target = "version")
    @Mapping(source = "createdAt", target = "publishedDate")
    @Mapping(source = "userId", target = "uploadedBy.userId")
    @Mapping(source = "username", target = "uploadedBy.username")
    VersionResponse mapToPlanVersionResponseMapper(VersionResult versionResult);
}
