package com.example.capstone_project.service.impl;

import com.example.capstone_project.entity.AnnualReport;
import com.example.capstone_project.entity.ExpenseStatus;
import com.example.capstone_project.entity.FinancialPlanExpense;
import com.example.capstone_project.entity.Report;
import com.example.capstone_project.repository.AnnualReportRepository;
import com.example.capstone_project.repository.FinancialPlanRepository;
import com.example.capstone_project.repository.result.AnnualReportResult;
import com.example.capstone_project.repository.result.ReportResult;
import com.example.capstone_project.service.GenerateAnnualReportService;
import com.example.capstone_project.utils.enums.ExpenseStatusCode;
import com.example.capstone_project.utils.mapper.annual.AnnualReportMapperImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GenerateAnnualReportServiceImpl implements GenerateAnnualReportService {
    private final AnnualReportRepository annualReportRepository;

    @Override
    public void generateAnnualReport() {

    }

//    @Scheduled(cron = "0 0 0 5 1 ?")
//    @Transactional
//    public void generateAnnualReport() {
//        // Generate annual report
//        AnnualReportResult annualReportResult = annualReportRepository.getAnnualReport(LocalDate.now(), ExpenseStatusCode.APPROVED);
//
//        AnnualReport annualReport = new AnnualReportMapperImpl().mapToAnnualReportMapping(annualReportResult);
//
//        // Generate annual list reports
//        List<ReportResult> reportResults = annualReportRepository.getListReports(LocalDate.now(), ExpenseStatusCode.APPROVED);
//        List<Report> reports = new ArrayList<>();
//        reportResults.forEach(reportResult -> {
//            Report report = new AnnualReportMapperImpl().mapToReportMapping(reportResult);
//            report.setAnnualReport(annualReport);
//            reports.add(report);
//        });
//
//        annualReport.setReports(reports);
//        annualReportRepository.save(annualReport);
//    }
}
