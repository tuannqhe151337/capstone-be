package com.example.capstone_project.utils.mapper.report;

import com.example.capstone_project.controller.responses.report.list.ReportResponse;
import com.example.capstone_project.entity.FinancialReport;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ReportPaginateResponseMapper {
    @Mapping(source = "id",target = "reportId")
    @Mapping(source = "name",target = "name")
    @Mapping(source = "version",target = "version")
    @Mapping(source = "status.id",target = "status.statusId")
    @Mapping(source = "status.name",target = "status.name")
    @Mapping(source = "term.id",target = "term.termId")
    @Mapping(source = "term.name",target = "term.name")
    @Mapping(source = "department.id",target = "department.departmentId")
    @Mapping(source = "department.name",target = "department.name")
    ReportResponse mapToReportResponseMapping(FinancialReport financialReport);
}
