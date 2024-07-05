package com.example.capstone_project.controller;

import com.example.capstone_project.controller.responses.ListPaginationResponse;
import com.example.capstone_project.controller.responses.ListResponse;
import com.example.capstone_project.controller.responses.Pagination;
import com.example.capstone_project.controller.responses.annualReport.list.AnnualReportResponse;
import com.example.capstone_project.controller.responses.annualReport.expenses.AnnualReportExpenseResponse;
import com.example.capstone_project.controller.responses.annualReport.expenses.CostTypeResponse;
import com.example.capstone_project.entity.AnnualReport;
import com.example.capstone_project.service.AnnualReportService;
import com.example.capstone_project.utils.helper.PaginationHelper;
import com.example.capstone_project.utils.mapper.annual.AnnualReportPaginateResponseMapperImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
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
    private final AnnualReportService annualReportService;

    @GetMapping("/expenses")
    public ResponseEntity<ListPaginationResponse<AnnualReportExpenseResponse>> confirmExpense() {
        ListPaginationResponse<AnnualReportExpenseResponse> listResponse = new ListPaginationResponse<>();
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
                .totalRecords(100)
                .page(10)
                .limitRecordsPerPage(0)
                .numPages(1)
                .build());

        return ResponseEntity.ok(listResponse);
    }

    @GetMapping("/list")
    public ResponseEntity<ListPaginationResponse<AnnualReportResponse>> getListAnnualReport(
            @RequestParam(required = false) String year,
            @RequestParam(required = false) String page,
            @RequestParam(required = false) String size,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String sortType
    ) {
        // Handling page and pageSize
        Integer pageInt = PaginationHelper.convertPageToInteger(page);
        Integer sizeInt = PaginationHelper.convertPageSizeToInteger(size);

        if (year == null) {
            year = "";
        }

        // Handling pagination
        Pageable pageable = PaginationHelper.handlingPagination(pageInt, sizeInt, sortBy, sortType);

        // Get data
        List<AnnualReport> annualReports = annualReportService.getListAnnualReportPaging(pageable, year);

        // Response
        ListPaginationResponse<AnnualReportResponse> response = new ListPaginationResponse<>();

        long count = 0;

        if (annualReports != null) {

            // Count total record
            count = annualReportService.countDistinctListAnnualReportPaging(year);

            // Mapping to TermPaginateResponse
            annualReports.forEach(annualReport -> response.getData().add(new AnnualReportPaginateResponseMapperImpl().mapToAnnualReportResponseMapping(annualReport)));

        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        long numPages = PaginationHelper.calculateNumPages(count, sizeInt);

        response.setPagination(Pagination.builder()
                .totalRecords(count)
                .page(pageInt)
                .limitRecordsPerPage(sizeInt)
                .numPages(numPages)
                .build());

        return ResponseEntity.ok(response);
    }
}
