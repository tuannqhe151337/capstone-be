package com.example.capstone_project.controller;

import com.example.capstone_project.controller.responses.ListResponse;
import com.example.capstone_project.controller.responses.Pagination;
import com.example.capstone_project.controller.responses.annualReport.list.AnnualReportResponse;
import com.example.capstone_project.controller.responses.annualReport.expenses.AnnualReportExpenseResponse;
import com.example.capstone_project.controller.responses.annualReport.expenses.CostTypeResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;


@RestController
@RequestMapping("/api/annual-report")
@RequiredArgsConstructor
public class AnnualReportController {
    @GetMapping("/expenses")
    public ResponseEntity<ListResponse<AnnualReportExpenseResponse>> confirmExpense() {
        ListResponse<AnnualReportExpenseResponse> listResponse = new ListResponse<>();
        listResponse.setData(
                List.of(
                        AnnualReportExpenseResponse.builder()
                                .expenseId(1L)
                                .departmentName("BU 1")
                                .totalExpenses(BigDecimal.valueOf(513123545))
                                .biggestExpenditure(BigDecimal.valueOf(5313215))
                                .costType(CostTypeResponse.builder()
                                        .costTypeId(1L)
                                        .name("Marketing").build())
                                .build(),
                        AnnualReportExpenseResponse.builder()
                                .expenseId(2L)
                                .departmentName("BU 2")
                                .totalExpenses(BigDecimal.valueOf(1234568623))
                                .biggestExpenditure(BigDecimal.valueOf(51453123))
                                .costType(CostTypeResponse.builder()
                                        .costTypeId(1L)
                                        .name("Marketing").build())
                                .build(),
                        AnnualReportExpenseResponse.builder()
                                .expenseId(3L)
                                .departmentName("BU 3")
                                .totalExpenses(BigDecimal.valueOf(12145641.25))
                                .biggestExpenditure(BigDecimal.valueOf(645554))
                                .costType(CostTypeResponse.builder()
                                        .costTypeId(1L)
                                        .name("Marketing").build())
                                .build()
                )
        );

        listResponse.setPagination(Pagination.builder()
                .count(100)
                .page(10)
                .displayRecord(0)
                .numPages(1)
                .build());

        return ResponseEntity.ok(listResponse);
    }

    @GetMapping("/list")
    public ResponseEntity<ListResponse<AnnualReportResponse>> getListAnnualReport(
            @RequestParam(required = false) String query,
            @RequestParam(required = false) String page,
            @RequestParam(required = false) String size,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String sortType
    ) {
        ListResponse<AnnualReportResponse> listResponse = new ListResponse<>();

        listResponse.setData(
                List.of(
                        AnnualReportResponse.builder()
                                .annualReportId(1L)
                                .name("Report 2022")
                                .totalTerm(12)
                                .totalExpense(BigDecimal.valueOf(213232523))
                                .totalDepartment(10)
                                .createDate(LocalDateTime.of(2023, 1, 5, 0, 0))
                                .build(),
                        AnnualReportResponse.builder()
                                .annualReportId(2L)
                                .name("Report 2021")
                                .totalTerm(15)
                                .totalExpense(BigDecimal.valueOf(213232523))
                                .totalDepartment(15)
                                .createDate(LocalDateTime.of(2022, 1, 5, 0, 0))
                                .build(),
                        AnnualReportResponse.builder()
                                .annualReportId(3L)
                                .name("Report 2020")
                                .totalTerm(12)
                                .totalExpense(BigDecimal.valueOf(213232523))
                                .totalDepartment(10)
                                .createDate(LocalDateTime.of(2021, 1, 5, 0, 0))
                                .build(),
                        AnnualReportResponse.builder()
                                .annualReportId(4L)
                                .name("Report 2019")
                                .totalTerm(12)
                                .totalExpense(BigDecimal.valueOf(213232523))
                                .totalDepartment(10)
                                .createDate(LocalDateTime.of(2020, 1, 5, 0, 0))
                                .build()
                )
        );

        listResponse.setPagination(Pagination.builder()
                .count(100)
                .page(10)
                .displayRecord(0)
                .numPages(1)
                .build());

        return ResponseEntity.ok(listResponse);
    }
}
