package com.example.capstone_project.controller;

import com.example.capstone_project.controller.responses.ListResponse;
import com.example.capstone_project.controller.responses.Pagination;
import com.example.capstone_project.controller.responses.annualReport.list.AnnualReportResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/annual-report")
@RequiredArgsConstructor
public class AnnualReportController {
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
