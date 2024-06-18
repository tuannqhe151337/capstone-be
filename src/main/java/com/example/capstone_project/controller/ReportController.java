package com.example.capstone_project.controller;

import com.example.capstone_project.controller.body.report.delete.DeleteReportBody;
import com.example.capstone_project.controller.responses.expense.CostTypeResponse;
import com.example.capstone_project.controller.responses.expense.list.ExpenseResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
    ){
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
            @RequestParam(required = false) Integer termId,
            @RequestParam(required = false) Integer departmentId,
            @RequestParam(required = false) Integer statusId,
            @RequestParam(required = false) String query,
            @RequestParam(required = false) String page,
            @RequestParam(required = false) String size,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String sortType
    ) {
        ListPaginationResponse<ReportResponse> listPaginationResponse = new ListPaginationResponse<>();
        listPaginationResponse.setData(List.of(
                        ReportResponse.builder()
                                .reportId(1L)
                                .name("BU name_Q1_report")
                                .status(StatusResponse.builder()
                                        .statusId(1L)
                                        .name("Approved").build()
                                )
                                .term(TermResponse.builder()
                                        .termId(1L)
                                        .name("Term name 1")
                                        .build())
                                .department(DepartmentResponse.builder()
                                        .departmentId(1L)
                                        .name("Department 1")
                                        .build())
                                .build(),
                        ReportResponse.builder()
                                .reportId(2L)
                                .name("BU name_Q2_report")
                                .status(StatusResponse.builder()
                                        .statusId(1L)
                                        .name("Approved").build()
                                )
                                .term(TermResponse.builder()
                                        .termId(2L)
                                        .name("Term name 2")
                                        .build())
                                .department(DepartmentResponse.builder()
                                        .departmentId(1L)
                                        .name("Department 1")
                                        .build())
                                .build(),
                        ReportResponse.builder()
                                .reportId(3L)
                                .name("BU name_Q3_report")
                                .status(StatusResponse.builder()
                                        .statusId(2L)
                                        .name("Reviewed").build()
                                )
                                .term(TermResponse.builder()
                                        .termId(1L)
                                        .name("Term name 1")
                                        .build())
                                .department(DepartmentResponse.builder()
                                        .departmentId(3L)
                                        .name("Department 3")
                                        .build())
                                .build(),
                        ReportResponse.builder()
                                .reportId(4L)
                                .name("BU name_Q4_report")
                                .status(StatusResponse.builder()
                                        .statusId(3L)
                                        .name("Denied").build()
                                )
                                .term(TermResponse.builder()
                                        .termId(1L)
                                        .name("Term name 1")
                                        .build())
                                .department(DepartmentResponse.builder()
                                        .departmentId(2L)
                                        .name("Department 2")
                                        .build())
                                .build()
                )
        );

        listPaginationResponse.setPagination(Pagination.builder()
                .totalRecords(777)
                .page(10)
                .limitRecordsPerPage(555)
                .numPages(1)
                .build());

        return ResponseEntity.ok(listPaginationResponse);
    }
}
