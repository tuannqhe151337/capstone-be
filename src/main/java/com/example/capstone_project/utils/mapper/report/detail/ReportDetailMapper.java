package com.example.capstone_project.utils.mapper.report.detail;

import com.example.capstone_project.controller.responses.report.detail.ReportDetailResponse;
import com.example.capstone_project.repository.result.ReportDetailResult;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ReportDetailMapper {
    @Mapping(expression = "java(reportDetailResult.getReportId())", target = "id")
    @Mapping(expression = "java(reportDetailResult.getName())", target = "name")
    @Mapping(expression = "java(reportDetailResult.getBiggestExpenditure())", target = "biggestExpenditure")
    @Mapping(expression = "java(reportDetailResult.getTotalCost())", target = "totalCost")
    @Mapping(expression = "java(reportDetailResult.getTermId())", target = "term.termId")
    @Mapping(expression = "java(reportDetailResult.getTermName())", target = "term.name")
    @Mapping(expression = "java(reportDetailResult.getStatusId())", target = "status.statusId")
    @Mapping(expression = "java(reportDetailResult.getStatusName())", target = "status.name")
    @Mapping(expression = "java(reportDetailResult.getPlanDueDate())", target = "planDueDate")
    @Mapping(expression = "java(reportDetailResult.getCreatedAt())", target = "createdAt")
    @Mapping(expression = "java(reportDetailResult.getDepartmentId())", target = "department.departmentId")
    @Mapping(expression = "java(reportDetailResult.getDepartmentName())", target = "department.name")
    @Mapping(expression = "java(reportDetailResult.getUserId())", target = "user.userId")
    @Mapping(expression = "java(reportDetailResult.getUsername())", target = "user.username")
    ReportDetailResponse mapToReportDetailResponseMapping(ReportDetailResult reportDetailResult);
}