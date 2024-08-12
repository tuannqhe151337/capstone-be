package com.example.capstone_project.service.impl;

import com.example.capstone_project.entity.AnnualReport;
import com.example.capstone_project.entity.Currency;
import com.example.capstone_project.entity.Report;
import com.example.capstone_project.entity.*;
import com.example.capstone_project.repository.*;
import com.example.capstone_project.repository.result.AnnualReportResult;
import com.example.capstone_project.repository.result.CostStatisticalByCurrencyResult;
import com.example.capstone_project.repository.result.PaginateExchange;
import com.example.capstone_project.repository.result.ReportResult;
import com.example.capstone_project.service.GenerateAnnualReportService;
import com.example.capstone_project.service.result.CostResult;
import com.example.capstone_project.service.result.TotalCostByCurrencyResult;
import com.example.capstone_project.utils.enums.ExpenseStatusCode;
import com.example.capstone_project.utils.mapper.annual.AnnualReportMapperImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@RequiredArgsConstructor
public class GenerateAnnualReportServiceImpl implements GenerateAnnualReportService {
    private final AnnualReportRepository annualReportRepository;
    private final ReportRepository reportRepository;

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

        List<Report> reports = new ArrayList<>();
        reportResults.forEach(reportResult -> {
            Report report = new AnnualReportMapperImpl().mapToReportMapping(reportResult);
            report.setAnnualReport(annualReport);
            reports.add(report);
        });

        annualReport.setReports(reports);
        annualReport.setTotalExpense(BigDecimal.valueOf(totalCostOfYear));

        annualReportRepository.save(annualReport);
        reportRepository.saveAll(reports);
    }
}
