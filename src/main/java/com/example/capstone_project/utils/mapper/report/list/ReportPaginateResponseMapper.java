package com.example.capstone_project.utils.mapper.report.list;

import com.example.capstone_project.controller.responses.report.list.ReportResponse;
import com.example.capstone_project.entity.FinancialReport;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ReportPaginateResponseMapper {
    @Mapping(source = "id", target = "reportId")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "status.id", target = "status.statusId")
    @Mapping(source = "status.name", target = "status.name")
    @Mapping(source = "term.id", target = "term.termId")
    @Mapping(source = "term.name", target = "term.name")
    ReportResponse mapToReportResponseMapping(FinancialReport financialReport);
}