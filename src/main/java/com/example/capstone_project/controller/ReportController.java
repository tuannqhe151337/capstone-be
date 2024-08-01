package com.example.capstone_project.controller;

import com.example.capstone_project.controller.body.report.delete.DeleteReportBody;
import com.example.capstone_project.controller.body.report.download.ReportDownloadBody;
import com.example.capstone_project.controller.responses.report.calculate.ReportActualCostResponse;
import com.example.capstone_project.controller.responses.report.calculate.ReportExpectedCostResponse;
import com.example.capstone_project.controller.responses.report.detail.ReportDetailResponse;
import com.example.capstone_project.controller.responses.report.expenses.ExpenseResponse;
import com.example.capstone_project.entity.FinancialPlanExpense;
import com.example.capstone_project.entity.FinancialReport;
import com.example.capstone_project.repository.result.ExpenseResult;
import com.example.capstone_project.repository.result.ReportDetailResult;
import com.example.capstone_project.repository.result.ReportExpenseResult;
import com.example.capstone_project.service.FinancialReportService;
import com.example.capstone_project.utils.exception.ResourceNotFoundException;
import com.example.capstone_project.utils.exception.UnauthorizedException;
import com.example.capstone_project.utils.helper.PaginationHelper;
import com.example.capstone_project.utils.mapper.report.detail.ReportDetailMapperImpl;
import com.example.capstone_project.utils.mapper.report.expenses.ReportExpenseResponseMapperImpl;
import com.example.capstone_project.utils.mapper.report.list.ReportPaginateResponseMapperImpl;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import com.example.capstone_project.controller.responses.ListPaginationResponse;
import com.example.capstone_project.controller.responses.Pagination;
import com.example.capstone_project.controller.responses.report.list.ReportResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/report")
@RequiredArgsConstructor
public class ReportController {
    private final FinancialReportService reportService;

//    @GetMapping("/expenses")
//    public ResponseEntity<ListPaginationResponse<ExpenseResponse>> getListExpense(
//            @RequestParam(required = true) Long reportId,
//            @RequestParam(required = false) Integer statusId,
//            @RequestParam(required = false) Integer costTypeId,
//            @RequestParam(required = false) String query,
//            @RequestParam(required = false) String page,
//            @RequestParam(required = false) String size,
//            @RequestParam(required = false) String sortBy,
//            @RequestParam(required = false) String sortType
//    ) throws Exception {
//        try {
//            // Handling page and pageSize
//            Integer pageInt = PaginationHelper.convertPageToInteger(page);
//            Integer sizeInt = PaginationHelper.convertPageSizeToInteger(size);
//            // Handling query
//            if (query == null) {
//                query = "";
//            }
//
//            // Handling pagination
//            Pageable pageable = PaginationHelper.handlingPagination(pageInt, sizeInt, sortBy, sortType);
//
//            // Get data
//            List<FinancialReportExpense> expenses = reportService.getListExpenseWithPaginate(reportId, query, statusId, costTypeId, pageable);
//
//            // Response
//            ListPaginationResponse<ExpenseResponse> response = new ListPaginationResponse<>();
//
//            long count = 0;
//
//            if (expenses != null) {
//
//                // Count total record
//                count = reportService.countDistinctListExpenseWithPaginate(query, reportId, statusId, costTypeId);
//
//                // Mapping to TermPaginateResponse
//                expenses.forEach(expense -> response.getData().add(new ReportExpenseResponseMapperImpl().mapToExpenseResponseMapping(expense)));
//            } else {
//                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
//            }
//
//            long numPages = PaginationHelper.calculateNumPages(count, sizeInt);
//
//            response.setPagination(Pagination.builder()
//                    .totalRecords(count)
//                    .page(pageInt)
//                    .limitRecordsPerPage(sizeInt)
//                    .numPages(numPages)
//                    .build());
//
//            return ResponseEntity.ok(response);
//        } catch (UnauthorizedException e) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
//        } catch (ResourceNotFoundException e) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
//        }
//    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteReport(
            @Valid @RequestBody DeleteReportBody reportBody
    ) {
        try {
            FinancialReport deletedReport = reportService.deleteReport(reportBody.getReportId());

            if (deletedReport == null) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
            }

            return ResponseEntity.status(HttpStatus.OK).body(null);
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);

        }
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
        try {
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
                count = reportService.countDistinctListReportPaginate(query, termId, statusId);

                // Mapping to TermPaginateResponse
                reports.forEach(report -> response.getData().add(new ReportPaginateResponseMapperImpl().mapToReportResponseMapping(report)));

            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
            }

            long numPages = PaginationHelper.calculateNumPages(count, sizeInt);

            response.setPagination(Pagination.builder()
                    .totalRecords(count)
                    .page(pageInt)
                    .limitRecordsPerPage(sizeInt)
                    .numPages(numPages)
                    .build());

            return ResponseEntity.ok(response);
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }

    @GetMapping("/detail")
    public ResponseEntity<ReportDetailResponse> getReportDetail(
            @RequestParam Long reportId
    ) throws Exception {
        try {
            // Get data
            ReportDetailResult report = reportService.getReportDetailByReportId(reportId);

            // Response
            ReportDetailResponse response;

            if (report != null) {
                // Mapping to PlanDetail Response
                response = new ReportDetailMapperImpl().mapToReportDetailResponseMapping(report);

            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
            }

            return ResponseEntity.ok(response);
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

    }

//    @PostMapping("/download/xlsx")
//    public ResponseEntity<byte[]> generateXlsxReport(
//            @Valid @RequestBody ReportDownloadBody reportBody
//    ) throws Exception {
//        try {
//            /// Get data for file Excel
//            byte[] report = reportService.getBodyFileExcelXLSX(reportBody.getReportId());
//            if (report != null) {
//                // Create file name for file Excel
//                String outFileName = reportService.generateXLSXFileName(reportBody.getReportId());
//
//                return createFileReportResponseEntity(report, outFileName);
//
//            } else {
//
//                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
//            }
//        } catch (UnauthorizedException e) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
//        } catch (ResourceNotFoundException e) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
//        }
//    }

//    @PostMapping("/download/xls")
//    public ResponseEntity<byte[]> generateXlsReport(
//            @Valid @RequestBody ReportDownloadBody reportBody
//    ) throws Exception {
//        try {
//            /// Get data for file Excel
//            byte[] report = reportService.getBodyFileExcelXLS(reportBody.getReportId());
//            if (report != null) {
//                // Create file name for file Excel
//                String outFileName = reportService.generateXLSFileName(reportBody.getReportId());
//
//                return createFileReportResponseEntity(report, outFileName);
//
//            } else {
//                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
//            }
//        } catch (UnauthorizedException e) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
//        } catch (ResourceNotFoundException e) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
//        }
//    }

    private ResponseEntity<byte[]> createFileReportResponseEntity(
            byte[] report, String fileName) {
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                .body(report);
    }

    @GetMapping("/expenses")
    public ResponseEntity<ListPaginationResponse<ExpenseResponse>> getListExpense(
            @RequestParam(required = true) Long reportId,
            @RequestParam(required = false) Integer departmentId,
            @RequestParam(required = false) Integer statusId,
            @RequestParam(required = false) Integer costTypeId,
            @RequestParam(required = false) String query,
            @RequestParam(required = false) String page,
            @RequestParam(required = false) String size,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String sortType
    ) throws Exception {
        try {
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
            List<ReportExpenseResult> expenses = reportService.getListExpenseWithPaginate(reportId, query, departmentId, statusId, costTypeId, pageable);

            // Response
            ListPaginationResponse<ExpenseResponse> response = new ListPaginationResponse<>();

            long count = 0;

            if (expenses != null) {

                // Count total record
                count = reportService.countDistinctListExpenseWithPaginate(query, reportId, departmentId, statusId, costTypeId);

                // Mapping to TermPaginateResponse
                expenses.forEach(expense -> response.getData().add(new ReportExpenseResponseMapperImpl().mapToReportExpenseResponseMapping(expense)));
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
            }

            long numPages = PaginationHelper.calculateNumPages(count, sizeInt);

            response.setPagination(Pagination.builder()
                    .totalRecords(count)
                    .page(pageInt)
                    .limitRecordsPerPage(sizeInt)
                    .numPages(numPages)
                    .build());

            return ResponseEntity.ok(response);
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @GetMapping("/actual-cost")
    public ResponseEntity<ReportActualCostResponse> calculateActualCost(
            @RequestParam Long reportId
    ) throws Exception {
        try {
            // Get data
            BigDecimal actualCost = reportService.calculateActualCostByReportId(reportId);

            // Response
            ReportActualCostResponse response = new ReportActualCostResponse();

            if (actualCost != null) {
                // Mapping to PlanDetail Response
                response.setActualCost(actualCost);

            } else {
                response.setActualCost(BigDecimal.valueOf(0L));
            }

            return ResponseEntity.ok(response);
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @GetMapping("/expected-cost")
    public ResponseEntity<ReportExpectedCostResponse> calculateExpectedCost(
            @RequestParam Long reportId
    ) throws Exception {
        try {
            // Get data
            BigDecimal expectedCost = reportService.calculateExpectedCostByReportId(reportId);

            // Response
            ReportExpectedCostResponse response = new ReportExpectedCostResponse();

            if (expectedCost != null) {
                // Mapping to PlanDetail Response
                response.setExpectedCost(expectedCost);

            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
            }

            return ResponseEntity.ok(response);
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

}
