package com.example.capstone_project.utils.mapper.annual;

import com.example.capstone_project.entity.AnnualReport;
import com.example.capstone_project.entity.MonthlyReportSummary;
import com.example.capstone_project.repository.result.AnnualReportResult;
import com.example.capstone_project.repository.result.ReportResult;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AnnualReportMapper {
    @Mapping(expression = "java(annualReportResult.getYear())",target = "year")
    @Mapping(expression = "java(annualReportResult.getTotalTerm())",target = "totalTerm")
    @Mapping(expression = "java(annualReportResult.getTotalDepartment())",target = "totalDepartment")
    AnnualReport mapToAnnualReportMapping(AnnualReportResult annualReportResult);

    @Mapping(expression = "java(reportResult.getDepartmentId())",target = "department.id")
    @Mapping(expression = "java(reportResult.getTotalExpense())",target = "totalExpense")
    @Mapping(expression = "java(reportResult.getBiggestExpense())",target = "biggestExpenditure")
    @Mapping(expression = "java(reportResult.getCostTypeId())",target = "costType.id")
    MonthlyReportSummary mapToReportMapping(ReportResult reportResult);
}