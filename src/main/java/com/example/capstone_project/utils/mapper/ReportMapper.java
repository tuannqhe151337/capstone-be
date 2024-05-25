package com.example.capstone_project.utils.mapper;

import com.example.capstone_project.controller.responses.ReportResponse;
import com.example.capstone_project.entity.Report;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ReportMapper {
    ReportResponse mapToReportResponse(Report report);
    Report mapToReport(ReportResponse reportResponse);
}
