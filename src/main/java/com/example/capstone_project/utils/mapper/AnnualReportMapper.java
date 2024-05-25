package com.example.capstone_project.utils.mapper;

import com.example.capstone_project.controller.responses.AnnualReportResponse;
import com.example.capstone_project.entity.AnnualReport;
import org.mapstruct.Mapper;
@Mapper(componentModel = "spring")
public interface AnnualReportMapper {
    AnnualReportResponse mapToAnnualReportResponse(AnnualReport annualReport);
    AnnualReport mapToAnnualReport(AnnualReportResponse annualReportResponse);
}
