package com.example.capstone_project.controller;

import com.example.capstone_project.controller.body.expense.ApprovalAllExpenseBody;
import com.example.capstone_project.controller.body.expense.ApprovalExpenseBody;
import com.example.capstone_project.controller.body.expense.DenyExpenseBody;
import com.example.capstone_project.controller.body.expense.UploadReportExpenses;
import com.example.capstone_project.controller.body.report.delete.DeleteReportBody;
import com.example.capstone_project.controller.responses.ExceptionResponse;
import com.example.capstone_project.controller.responses.ListResponse;
import com.example.capstone_project.controller.responses.report.CostTypeResponse;
import com.example.capstone_project.controller.responses.report.DepartmentResponse;
import com.example.capstone_project.controller.responses.report.diagram.CostTypeDiagramResponse;
import com.example.capstone_project.controller.responses.report.CurrencyResponse;
import com.example.capstone_project.controller.responses.report.calculate.ReportActualCostResponse;
import com.example.capstone_project.controller.responses.report.calculate.ReportExpectedCostResponse;
import com.example.capstone_project.controller.responses.report.detail.ReportDetailResponse;
import com.example.capstone_project.controller.responses.report.diagram.DepartmentDiagramResponse;
import com.example.capstone_project.controller.responses.report.diagram.YearDiagramResponse;
import com.example.capstone_project.controller.responses.report.expenses.ExpenseResponse;
import com.example.capstone_project.entity.ExpenseStatus;
import com.example.capstone_project.entity.FinancialPlanExpense;
import com.example.capstone_project.entity.FinancialReport;
import com.example.capstone_project.repository.result.*;
import com.example.capstone_project.service.FinancialReportService;
import com.example.capstone_project.service.result.CostResult;
import com.example.capstone_project.utils.exception.InvalidInputException;
import com.example.capstone_project.utils.exception.ResourceNotFoundException;
import com.example.capstone_project.utils.exception.UnauthorizedException;
import com.example.capstone_project.utils.helper.PaginationHelper;
import com.example.capstone_project.utils.helper.RemoveDuplicateHelper;
import com.example.capstone_project.utils.mapper.annual.CostTypeDiagramMapperImpl;
import com.example.capstone_project.utils.mapper.report.detail.ReportDetailMapperImpl;
import com.example.capstone_project.utils.mapper.report.expenses.ReportExpenseResponseMapperImpl;
import com.example.capstone_project.utils.mapper.report.list.ReportPaginateResponseMapperImpl;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import com.example.capstone_project.controller.responses.ListPaginationResponse;
import com.example.capstone_project.controller.responses.Pagination;
import com.example.capstone_project.controller.responses.report.list.ReportResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
    public ResponseEntity<ExceptionResponse> deleteReport(
            @Valid @RequestBody DeleteReportBody reportBody, BindingResult bindingResult
    ) {

        if (bindingResult.hasErrors()) {
            // Xử lý lỗi validation và trả về phản hồi lỗi
            String errorMessage = bindingResult.getAllErrors().stream()
                    .map(ObjectError::getDefaultMessage)
                    .collect(Collectors.joining(", "));
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    ExceptionResponse.builder().field("Validation Error").message(errorMessage).build());
        }

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
                // Mapping to report Detail Response
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

    @GetMapping("/download-xlsx")
    public ResponseEntity<byte[]> generateXlsxReport(
            @RequestParam(required = true) Long reportId
    ) throws Exception {
        try {
            /// Get data for file Excel
            byte[] report = reportService.getBodyFileExcelXLSX(reportId);
            if (report != null) {
                // Create file name for file Excel
                String outFileName = reportService.generateXLSXFileName(reportId);

                return createFileReportResponseEntity(report, outFileName);

            } else {

                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
            }
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @GetMapping("/download-xls")
    public ResponseEntity<byte[]> generateXlsReport(
            @RequestParam(required = true) Long reportId
    ) throws Exception {
        try {
            /// Get data for file Excel
            byte[] report = reportService.getBodyFileExcelXLS(reportId);
            if (report != null) {
                // Create file name for file Excel
                String outFileName = reportService.generateXLSFileName(reportId);

                return createFileReportResponseEntity(report, outFileName);

            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
            }
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

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
            @RequestParam(required = false) Long currencyId,
            @RequestParam(required = false) Integer departmentId,
            @RequestParam(required = false) Integer statusId,
            @RequestParam(required = false) Integer costTypeId,
            @RequestParam(required = false) Integer projectId,
            @RequestParam(required = false) Integer supplierId,
            @RequestParam(required = false) Integer picId,
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
            List<ReportExpenseResult> expenses = reportService.getListExpenseWithPaginate(reportId, query, departmentId, statusId, costTypeId, projectId, supplierId, picId, currencyId, pageable);

            // Response
            ListPaginationResponse<ExpenseResponse> response = new ListPaginationResponse<>();

            long count = 0;

            if (expenses != null) {

                // Count total record
                count = reportService.countDistinctListExpenseWithPaginate(query, reportId, departmentId, statusId, costTypeId, projectId, supplierId, picId);

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
            CostResult actualCost = reportService.calculateActualCostByReportId(reportId);

            // Response
            ReportActualCostResponse response = new ReportActualCostResponse();

            if (actualCost != null) {
                // Mapping to report Detail Response

                response.setCost(actualCost.getCost());
                response.setCurrency(CurrencyResponse.builder()
                        .currencyId(actualCost.getCurrency().getId())
                        .name(actualCost.getCurrency().getName())
                        .symbol(actualCost.getCurrency().getSymbol())
                        .affix(actualCost.getCurrency().getAffix())
                        .build());
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
            CostResult expectedCost = reportService.calculateExpectedCostByReportId(reportId);

            // Response
            ReportExpectedCostResponse response = new ReportExpectedCostResponse();

            if (expectedCost != null) {
                // Mapping to report Detail Response
                response.setCost(expectedCost.getCost());
                response.setCurrency(CurrencyResponse.builder()
                        .currencyId(expectedCost.getCurrency().getId())
                        .name(expectedCost.getCurrency().getName())
                        .symbol(expectedCost.getCurrency().getSymbol())
                        .affix(expectedCost.getCurrency().getAffix())
                        .build());

            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
            }

            return ResponseEntity.ok(response);
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PutMapping("/expense-approval")
    public ResponseEntity<ExceptionResponse> approvalExpenses(
            @Valid @RequestBody ApprovalExpenseBody body, BindingResult bindingResult) throws Exception {

        if (bindingResult.hasErrors()) {
            // Xử lý lỗi validation và trả về phản hồi lỗi
            String errorMessage = bindingResult.getAllErrors().stream()
                    .map(ObjectError::getDefaultMessage)
                    .collect(Collectors.joining(", "));
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    ExceptionResponse.builder().field("Validation Error").message(errorMessage).build());
        }

        try {

            reportService.approvalExpenses(body.getReportId(), body.getListExpenseId());

            return ResponseEntity.status(HttpStatus.OK).body(ExceptionResponse.builder().field("Update Successful").message("Approval expense successful.").build());
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ExceptionResponse.builder().field("Unauthorized Error").message("User not allowed to approval expense").build());
        } catch (InvalidInputException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ExceptionResponse.builder().field("Validation Error").message("Your list expense id invalid or can not re-upload plan in this time period").build());
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ExceptionResponse.builder().field("Validation Error").message("Not found any report have id = " + body.getReportId() + " or list expense is empty").build());
        }
    }

    @PutMapping("/expense-deny")
    public ResponseEntity<ExceptionResponse> denyExpenses(
            @Valid @RequestBody DenyExpenseBody body, BindingResult bindingResult) throws Exception {

        if (bindingResult.hasErrors()) {
            // Xử lý lỗi validation và trả về phản hồi lỗi
            String errorMessage = bindingResult.getAllErrors().stream()
                    .map(ObjectError::getDefaultMessage)
                    .collect(Collectors.joining(", "));
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    ExceptionResponse.builder().field("Validation Error").message(errorMessage).build());
        }

        try {

            reportService.denyExpenses(body.getReportId(), body.getListExpenseId());

            return ResponseEntity.status(HttpStatus.OK).body(ExceptionResponse.builder().field("Update Successful").message("Deny expense successful.").build());
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ExceptionResponse.builder().field("Unauthorized Error").message("User not allowed to approval expense").build());
        } catch (InvalidInputException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ExceptionResponse.builder().field("Validation Error").message("Your list expense id invalid or can not re-upload plan in this time period").build());
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ExceptionResponse.builder().field("Validation Error").message("Not found any report have id = " + body.getReportId() + " or list expense is empty").build());
        }
    }

    @PutMapping("/expense-approval-all")
    public ResponseEntity<ExceptionResponse> approvalAllExpenses(
            @Valid @RequestBody ApprovalAllExpenseBody body, BindingResult bindingResult) throws Exception {

        if (bindingResult.hasErrors()) {
            // Xử lý lỗi validation và trả về phản hồi lỗi
            String errorMessage = bindingResult.getAllErrors().stream()
                    .map(ObjectError::getDefaultMessage)
                    .collect(Collectors.joining(", "));
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    ExceptionResponse.builder().field("Validation Error").message(errorMessage).build());
        }

        try {

            reportService.approvalAllExpenses(body.getReportId());

            return ResponseEntity.status(HttpStatus.OK).body(ExceptionResponse.builder().field("Update Successful").message("Approval all expense successful.").build());
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ExceptionResponse.builder().field("Unauthorized Error").message("User not allowed to approval expense").build());
        } catch (InvalidInputException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ExceptionResponse.builder().field("Validation Error").message("Your list expense id invalid or can not re-upload plan in this time period").build());
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ExceptionResponse.builder().field("Validation Error").message("Not found any report have id = " + body.getReportId() + " or list expense is empty").build());
        }
    }

    @PostMapping("/upload")
    public ResponseEntity<ExceptionResponse> uploadReportExpenses(
            @Valid @RequestBody UploadReportExpenses body, BindingResult bindingResult) throws Exception {

        if (bindingResult.hasErrors()) {
            // Xử lý lỗi validation và trả về phản hồi lỗi
            String errorMessage = bindingResult.getAllErrors().stream()
                    .map(ObjectError::getDefaultMessage)
                    .collect(Collectors.joining(", "));
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    ExceptionResponse.builder().field("Validation Error").message(errorMessage).build());
        }

        try {
            List<FinancialPlanExpense> rawExpenses = new ArrayList<>();
            body.setListExpenses(RemoveDuplicateHelper.removeDuplicateCodes(body.getListExpenses()));

            body.getListExpenses().forEach(expenseBody -> {
                rawExpenses.add(FinancialPlanExpense.builder()
                        .id(expenseBody.getExpenseId())
                        .status(ExpenseStatus.builder()
                                .code(expenseBody.getStatusCode())
                                .build())
                        .build());
            });

            reportService.uploadReportExpenses(body.getReportId(), rawExpenses);

            return ResponseEntity.status(HttpStatus.OK).body(ExceptionResponse.builder().field("Update Successful").message("Upload file report successful.").build());
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ExceptionResponse.builder().field("Unauthorized Error").message("User not allowed to approval expense").build());
        } catch (InvalidInputException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ExceptionResponse.builder().field("Validation Error").message("Your list expense id invalid or can not re-upload plan in this time period").build());
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ExceptionResponse.builder().field("Validation Error").message("Not found any report have id = " + body.getReportId() + " or list expense is empty").build());
        }
    }

    @GetMapping("/year-diagram")
    public ResponseEntity<ListResponse<YearDiagramResponse>> getYearDiagram(
            @RequestParam(required = true) Integer year
    ) {
        try {
            // Get data
            List<YearDiagramResult> yearDiagramResults = reportService.generateYearDiagram(year);

            // Response
            ListResponse<YearDiagramResponse> response = new ListResponse<>();

            if (yearDiagramResults != null) {

                yearDiagramResults.forEach(yearDiagramResult -> response.getData().add(YearDiagramResponse.builder()
                        .month(yearDiagramResult.getMonth())
                        .actualCost(yearDiagramResult.getActualCost())
                        .expectedCost(yearDiagramResult.getExpectedCost())
                        .build()));

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

    @GetMapping("/cost-type-year-diagram")
    public ResponseEntity<ListResponse<CostTypeDiagramResponse>> getCostTypeYearDiagram(
            @RequestParam(required = true) Integer year
    ) {
        try {
            // Get data
            List<CostTypeDiagramResult> costTypeDiagrams = reportService.getYearCostTypeDiagram(year);

            // Response
            ListResponse<CostTypeDiagramResponse> response = new ListResponse<>();

            if (costTypeDiagrams != null) {

                costTypeDiagrams.forEach(costTypeDiagram -> response.getData().add(CostTypeDiagramResponse.builder()
                        .totalCost(costTypeDiagram.getTotalCost())
                        .costType(CostTypeResponse.builder()
                                .costTypeId(costTypeDiagram.getCostTypeId())
                                .name(costTypeDiagram.getCostTypeName())
                                .build()).build()));

            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
            }

            return ResponseEntity.ok(response);
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/department-year-diagram")
    public ResponseEntity<ListResponse<DepartmentDiagramResponse>> getDepartmentYearDiagram(
            @RequestParam(required = true) Integer year
    ) {
        try {
            // Get data
            List<DepartmentDiagramResult> departmentDiagramResults = reportService.getYearDepartmentDiagram(year);

            // Response
            ListResponse<DepartmentDiagramResponse> response = new ListResponse<>();

            if (departmentDiagramResults != null) {

                departmentDiagramResults.forEach(costTypeDiagram -> response.getData().add(DepartmentDiagramResponse.builder()
                        .totalCost(costTypeDiagram.getTotalCost())
                        .department(DepartmentResponse.builder()
                                .departmentId(costTypeDiagram.getDepartmentId())
                                .name(costTypeDiagram.getDepartmentName())
                                .build()).build()));

            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
            }

            return ResponseEntity.ok(response);
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/cost-type-report-diagram")
    public ResponseEntity<ListResponse<CostTypeDiagramResponse>> getCostTypeReportDiagram(
            @RequestParam(required = true) Long reportId
    ) {
        try {
            // Get data
            List<CostTypeDiagramResult> costTypeDiagrams = reportService.getReportCostTypeDiagram(reportId);

            // Response
            ListResponse<CostTypeDiagramResponse> response = new ListResponse<>();

            if (costTypeDiagrams != null) {

                costTypeDiagrams.forEach(costTypeDiagram -> response.getData().add(CostTypeDiagramResponse.builder()
                        .totalCost(costTypeDiagram.getTotalCost())
                        .costType(CostTypeResponse.builder()
                                .costTypeId(costTypeDiagram.getCostTypeId())
                                .name(costTypeDiagram.getCostTypeName())
                                .build()).build()));

            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
            }

            return ResponseEntity.ok(response);
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/department-report-diagram")
    public ResponseEntity<ListResponse<DepartmentDiagramResponse>> getDepartmentReportDiagram(
            @RequestParam(required = true) Long reportId
    ) {
        try {
            // Get data
            List<DepartmentDiagramResult> departmentDiagramResults = reportService.getReportDepartmentDiagram(reportId);

            // Response
            ListResponse<DepartmentDiagramResponse> response = new ListResponse<>();

            if (departmentDiagramResults != null) {

                departmentDiagramResults.forEach(costTypeDiagram -> response.getData().add(DepartmentDiagramResponse.builder()
                        .totalCost(costTypeDiagram.getTotalCost())
                        .department(DepartmentResponse.builder()
                                .departmentId(costTypeDiagram.getDepartmentId())
                                .name(costTypeDiagram.getDepartmentName())
                                .build()).build()));

            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
            }

            return ResponseEntity.ok(response);
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
