package com.example.capstone_project.controller;

import com.example.capstone_project.controller.responses.ListResponse;
import com.example.capstone_project.controller.responses.Pagination;
import com.example.capstone_project.controller.responses.annualReport.expenses.AnnualReportExpenseResponse;
import com.example.capstone_project.controller.responses.annualReport.expenses.CostTypeResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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
}
