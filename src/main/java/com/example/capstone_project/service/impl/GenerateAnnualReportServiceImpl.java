package com.example.capstone_project.service.impl;

import com.example.capstone_project.entity.AnnualReport;
import com.example.capstone_project.entity.MonthlyReportSummary;
import com.example.capstone_project.repository.*;
import com.example.capstone_project.repository.result.AnnualReportResult;
import com.example.capstone_project.repository.result.ReportResult;
import com.example.capstone_project.service.GenerateAnnualReportService;
import com.example.capstone_project.utils.mapper.annual.AnnualReportMapperImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

@Service
@RequiredArgsConstructor
public class GenerateAnnualReportServiceImpl implements GenerateAnnualReportService {
    private final AnnualReportRepository annualReportRepository;
    private final MonthlyReportSummaryRepository monthlyReportSummaryRepository;

    // Chạy vào ngày 25 tháng 12 hàng năm
    @Scheduled(cron = "0 0 0 25 12 ?")
    @Transactional
    public void generateAnnualReport() {

        // Generate annual report
        AnnualReportResult annualReportResult = annualReportRepository.getAnnualReport(LocalDate.now());

        AnnualReport annualReport = new AnnualReportMapperImpl().mapToAnnualReportMapping(annualReportResult);

        // Generate report for annual report
        List<ReportResult> reportResults = annualReportRepository.generateReport(LocalDate.now());

        long totalCostOfYear = 0L;

        for (ReportResult reportResult : reportResults) {
            totalCostOfYear += reportResult.getTotalExpense().longValue();
        }

        List<MonthlyReportSummary> monthlyReportSummaries = new ArrayList<>();
        reportResults.forEach(reportResult -> {
            MonthlyReportSummary monthlyReportSummary = new AnnualReportMapperImpl().mapToReportMapping(reportResult);
            monthlyReportSummary.setAnnualReport(annualReport);
            monthlyReportSummaries.add(monthlyReportSummary);
        });

        annualReport.setMonthlyReportSummaries(monthlyReportSummaries);
        annualReport.setTotalExpense(BigDecimal.valueOf(totalCostOfYear));

        annualReportRepository.save(annualReport);
        monthlyReportSummaryRepository.saveAll(monthlyReportSummaries);
    }
}
