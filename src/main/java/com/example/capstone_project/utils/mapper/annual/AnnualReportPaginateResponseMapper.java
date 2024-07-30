package com.example.capstone_project.utils.mapper.annual;

import com.example.capstone_project.controller.responses.annualReport.list.AnnualReportResponse;
import com.example.capstone_project.entity.AnnualReport;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AnnualReportPaginateResponseMapper {
    @Mapping(source = "id", target = "annualReportId")
    @Mapping(source = "createdAt", target = "createDate")
    AnnualReportResponse mapToAnnualReportResponseMapping(AnnualReport annualReport);
}
