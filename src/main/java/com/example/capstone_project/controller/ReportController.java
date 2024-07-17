package com.example.capstone_project.controller;

import com.example.capstone_project.controller.body.plan.download.PlanDownloadBody;
import com.example.capstone_project.controller.body.report.delete.DeleteReportBody;
import com.example.capstone_project.controller.body.report.download.ReportDownloadBody;
import com.example.capstone_project.controller.responses.expense.CostTypeResponse;
import com.example.capstone_project.controller.responses.expense.list.ExpenseResponse;
import com.example.capstone_project.controller.responses.plan.list.PlanResponse;
import com.example.capstone_project.entity.FinancialPlan;
import com.example.capstone_project.entity.FinancialReport;
import com.example.capstone_project.service.FinancialReportService;
import com.example.capstone_project.utils.exception.ResourceNotFoundException;
import com.example.capstone_project.utils.exception.UnauthorizedException;
import com.example.capstone_project.utils.helper.PaginationHelper;
import com.example.capstone_project.utils.mapper.plan.list.ListPlanResponseMapperImpl;
import com.example.capstone_project.utils.mapper.report.ReportPaginateResponseMapperImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import com.example.capstone_project.controller.responses.ListPaginationResponse;
import com.example.capstone_project.controller.responses.Pagination;
import com.example.capstone_project.controller.responses.report.list.DepartmentResponse;
import com.example.capstone_project.controller.responses.report.list.ReportResponse;
import com.example.capstone_project.controller.responses.report.list.StatusResponse;
import com.example.capstone_project.controller.responses.report.list.TermResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/report")
@RequiredArgsConstructor
public class ReportController {
    private final FinancialReportService reportService;

    @GetMapping("/expenses")
    public ResponseEntity<ListPaginationResponse<ExpenseResponse>> getListExpense(
            @RequestParam(required = false) Integer departmentId,
            @RequestParam(required = false) Integer statusId,
            @RequestParam(required = false) Integer costTypeId,
            @RequestParam(required = false) String query,
            @RequestParam(required = false) String page,
            @RequestParam(required = false) String size,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String sortType
    ) {
        ListPaginationResponse<ExpenseResponse> listResponse = new ListPaginationResponse<>();
        listResponse.setData(List.of(
                ExpenseResponse.builder()
                        .expenseId(1L)
                        .name("Promotion event")
                        .costType(CostTypeResponse.builder()
                                .costTypeId(1L)
                                .name("Direct cost").build())
                        .unitPrice(BigDecimal.valueOf(15000000))
                        .amount(3)
                        .projectName("RECT")
                        .supplierName("Hong Ha")
                        .pic("HongHD9")
                        .notes("Approximate")
                        .status(com.example.capstone_project.controller.responses.expense.list.StatusResponse.builder()
                                .statusId(1L)
                                .name("Waiting for approval").build())
                        .build(),
                ExpenseResponse.builder()
                        .expenseId(2L)
                        .name("Social media")
                        .costType(CostTypeResponse.builder()
                                .costTypeId(1L)
                                .name("Direct cost").build())
                        .unitPrice(BigDecimal.valueOf(15000000))
                        .amount(1)
                        .projectName("IN22")
                        .supplierName("Hong Ha")
                        .pic("HongHD9")
                        .status(com.example.capstone_project.controller.responses.expense.list.StatusResponse.builder()
                                .statusId(2L)
                                .name("Waiting for approval").build())
                        .build(),
                ExpenseResponse.builder()
                        .expenseId(3L)
                        .name("Office supplier")
                        .costType(CostTypeResponse.builder()
                                .costTypeId(2L)
                                .name("Adminstration").build())
                        .unitPrice(BigDecimal.valueOf(5000000))
                        .amount(2)
                        .projectName("CAM1")
                        .supplierName("TuNM")
                        .pic("TuanVV")
                        .status(com.example.capstone_project.controller.responses.expense.list.StatusResponse.builder()
                                .statusId(1L)
                                .name("Waiting for approval").build())
                        .build()
        ));

        listResponse.setPagination(Pagination.builder()
                .totalRecords(100)
                .page(10)
                .limitRecordsPerPage(0)
                .numPages(1)
                .build());

        return ResponseEntity.ok(listResponse);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteReport(
            @Valid @RequestBody DeleteReportBody reportBody
    ) {
        System.out.println(reportBody.toString());
        return null;
    }

    @GetMapping("/list")
    public ResponseEntity<ListPaginationResponse<ReportResponse>> getListReport(
            @RequestParam(required = false) Long termId,
            @RequestParam(required = false) Long departmentId,
            @RequestParam(required = false) Long statusId,
            @RequestParam(required = false) String query,
            @RequestParam(required = false) String page,
            @RequestParam(required = false) String size,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String sortType
    ) throws Exception {
        // Handling page and pageSize
        Integer pageInt = PaginationHelper.convertPageToInteger(page);
        Integer sizeInt = PaginationHelper.convertPageSizeToInteger(size);

        // Handling query
        if (query == null) {
            query = "";
        }

        // Handling pagination
        Pageable pageable = PaginationHelper.handlingPagination(pageInt, sizeInt, sortBy, sortType);

        // Get data
        List<FinancialReport> reports = reportService.getListReportPaginate(query, termId, departmentId, statusId, pageable);

        // Response
        ListPaginationResponse<ReportResponse> response = new ListPaginationResponse<>();

        long count = 0;

        if (reports != null) {

            // Count total record
            count = reportService.countDistinctListReportPaginate(query, termId, departmentId, statusId);

            // Mapping to TermPaginateResponse
            reports.forEach(report -> response.getData().add(new ReportPaginateResponseMapperImpl().mapToReportResponseMapping(report)));

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

    @PostMapping("/download/xlsx")
    public ResponseEntity<byte[]> generateXlsxReport(
            @Valid @RequestBody ReportDownloadBody reportBody
    ) throws Exception {
        try {
            /// Get data for file Excel
            byte[] report = reportService.getBodyFileExcelXLSX(reportBody.getReportId());
            if (report != null) {
                // Create file name for file Excel
                String outFileName = reportService.generateXLSXFileName(reportBody.getReportId());

                return createFileReportResponseEntity(report, outFileName);

            } else {

                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
            }
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PostMapping("/download/xls")
    public ResponseEntity<byte[]> generateXlsReport(
            @Valid @RequestBody ReportDownloadBody reportBody
    ) throws Exception {
        try {
            /// Get data for file Excel
            byte[] report = reportService.getBodyFileExcelXLS(reportBody.getReportId());
            if (report != null) {
                // Create file name for file Excel
                String outFileName = reportService.generateXLSFileName(reportBody.getReportId());

                return createFileReportResponseEntity(report, outFileName);

            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
            }
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    private ResponseEntity<byte[]> createFileReportResponseEntity(
            byte[] report, String fileName) {
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                .body(report);
    }
}
