package com.example.capstone_project.utils.mapper.term_report;
import com.example.capstone_project.controller.responses.term.getReports.TermReportResponse;
import com.example.capstone_project.entity.FinancialReport;
import com.example.capstone_project.entity.MonthlyReportSummary;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface FinancialReportEntityToTermReportResponseMapper {


    TermReportResponse mapReportEntityToTermReportResponse(FinancialReport report);

    List<TermReportResponse> mapPlanEntityToTermPlanResponseList(List<MonthlyReportSummary> monthlyReportSummaryList);
}
