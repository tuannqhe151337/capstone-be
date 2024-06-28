package com.example.capstone_project.controller;

import com.example.capstone_project.controller.body.report.delete.DeleteReportBody;
import com.example.capstone_project.controller.body.report.expenses.ReportExpensesBody;
import com.example.capstone_project.controller.responses.expense.CostTypeResponse;
import com.example.capstone_project.controller.responses.expense.list.ExpenseResponse;
import com.example.capstone_project.controller.responses.plan.list.PlanResponse;
import com.example.capstone_project.entity.FinancialPlan;
import com.example.capstone_project.entity.FinancialReport;
import com.example.capstone_project.entity.FinancialReportExpense;
import com.example.capstone_project.service.FinancialReportService;
import com.example.capstone_project.utils.helper.PaginationHelper;
import com.example.capstone_project.utils.mapper.plan.list.ListPlanResponseMapperImpl;
import com.example.capstone_project.utils.mapper.report.ReportPaginateResponseMapperImpl;
import com.example.capstone_project.utils.mapper.report.expenses.ExpenseResponseMapperImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
            @RequestBody ReportExpensesBody reportBody,
            @RequestParam(required = false) Integer statusId,
            @RequestParam(required = false) Integer costTypeId,
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
        List<FinancialReportExpense> expenses = reportService.getListExpenseWithPaginate(reportBody.getReportId(), query, statusId, costTypeId, pageable);

        // Response
        ListPaginationResponse<ExpenseResponse> response = new ListPaginationResponse<>();

        long count = 0;

        if (expenses != null) {

            // Count total record
            count = reportService.countDistinctListExpenseWithPaginate(query, reportBody.getReportId(), statusId, costTypeId);

            // Mapping to TermPaginateResponse
            expenses.forEach(expense -> response.getData().add(new ExpenseResponseMapperImpl().mapToExpenseResponseMapping(expense)));
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
}
