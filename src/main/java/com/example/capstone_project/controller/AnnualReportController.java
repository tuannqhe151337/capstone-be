package com.example.capstone_project.controller;

import com.example.capstone_project.controller.responses.ListPaginationResponse;
import com.example.capstone_project.controller.responses.ListResponse;
import com.example.capstone_project.controller.responses.Pagination;
import com.example.capstone_project.controller.responses.annualReport.diagram.CostTypeDiagramResponse;
import com.example.capstone_project.controller.responses.annualReport.list.AnnualReportResponse;
import com.example.capstone_project.controller.responses.annualReport.expenses.AnnualReportExpenseResponse;
import com.example.capstone_project.entity.AnnualReport;
import com.example.capstone_project.entity.Report;
import com.example.capstone_project.repository.result.CostTypeDiagramResult;
import com.example.capstone_project.service.AnnualReportService;
import com.example.capstone_project.utils.exception.ResourceNotFoundException;
import com.example.capstone_project.utils.exception.UnauthorizedException;
import com.example.capstone_project.utils.helper.PaginationHelper;
import com.example.capstone_project.utils.mapper.annual.AnnualReportExpenseMapperImpl;
import com.example.capstone_project.utils.mapper.annual.AnnualReportPaginateResponseMapperImpl;
import com.example.capstone_project.utils.mapper.annual.CostTypeDiagramMapperImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/annual-report")
@RequiredArgsConstructor
public class AnnualReportController {
    private final AnnualReportService annualReportService;

    @GetMapping("/expenses")
    public ResponseEntity<ListPaginationResponse<AnnualReportExpenseResponse>> confirmExpense(
            @RequestParam(required = true) Long annualReportId,
            @RequestParam(required = false) Long costTypeId,
            @RequestParam(required = false) Long departmentId,
            @RequestParam(required = false) String page,
            @RequestParam(required = false) String size,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String sortType
    ) {
        try {
            // Handling page and pageSize
            Integer pageInt = PaginationHelper.convertPageToInteger(page);
            Integer sizeInt = PaginationHelper.convertPageSizeToInteger(size);


            // Handling pagination
            Pageable pageable = PaginationHelper.handlingPagination(pageInt, sizeInt, sortBy, sortType);

            // Get data
            List<Report> reports = annualReportService.getListExpenseWithPaginate(annualReportId, costTypeId, departmentId, pageable);

            // Response
            ListPaginationResponse<AnnualReportExpenseResponse> response = new ListPaginationResponse<>();

            long count = 0;

            if (reports != null) {

                // Count total record
                count = annualReportService.countDistinctListExpenseWithPaginate(annualReportId, costTypeId, departmentId);

                // Mapping to TermPaginateResponse
                reports.forEach(report -> response.getData().add(new AnnualReportExpenseMapperImpl().mapToAnnualReportExpenseResponseMapping(report)));

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

    @GetMapping("/list")
    public ResponseEntity<ListPaginationResponse<AnnualReportResponse>> getListAnnualReport(
            @RequestParam(required = false) String year,
            @RequestParam(required = false) String page,
            @RequestParam(required = false) String size,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String sortType
    ) {
        try {
            // Handling page and pageSize
            Integer pageInt = PaginationHelper.convertPageToInteger(page);
            Integer sizeInt = PaginationHelper.convertPageSizeToInteger(size);

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
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }

    @GetMapping("/diagram")
    public ResponseEntity<ListResponse<CostTypeDiagramResponse>> getAnnualReportDiagram(
            @RequestParam(required = true) Long annualReportId
    ) {
        try {
            // Get data
            List<CostTypeDiagramResult> costTypeDiagrams = annualReportService.getAnnualReportCostTypeDiagram(annualReportId);

            // Response
            ListResponse<CostTypeDiagramResponse> response = new ListResponse<>();

            if (costTypeDiagrams != null) {

                costTypeDiagrams.forEach(costTypeDiagram -> response.getData().add(new CostTypeDiagramMapperImpl().mapToCostTypeDiagramResponseMapping(costTypeDiagram)));

            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
            }

            return ResponseEntity.ok(response);
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @GetMapping("/detail")
    public ResponseEntity<AnnualReportResponse> getAnnualReportDetail(
            @RequestParam(required = true) Long annualReportId
    ) {
        try {
            // Get data
            AnnualReport annualReport = annualReportService.getAnnualReportDetail(annualReportId);

            // Response
            AnnualReportResponse response = null;

            if (annualReport != null) {

                response = new AnnualReportPaginateResponseMapperImpl().mapToAnnualReportResponseMapping(annualReport);

            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
            }

            return ResponseEntity.ok(response);
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
}
