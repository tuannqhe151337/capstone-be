package com.example.capstone_project.utils.mapper.term_report;
import com.example.capstone_project.controller.responses.term.getPlans.TermPlanDetailResponse;
import com.example.capstone_project.controller.responses.term.getReports.TermReportResponse;
import com.example.capstone_project.entity.FinancialPlan;
import com.example.capstone_project.entity.FinancialReport;
import com.example.capstone_project.entity.Report;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface FinancialReportEntityToTermReportResponseMapper {


    TermReportResponse mapReportEntityToTermReportResponse(FinancialReport report);

    List<TermReportResponse> mapPlanEntityToTermPlanResponseList(List<Report> reportList);
}
