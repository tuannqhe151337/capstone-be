package com.example.capstone_project.controller;

import com.example.capstone_project.controller.body.report.delete.DeleteReportBody;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.capstone_project.controller.responses.ListResponse;
import com.example.capstone_project.controller.responses.Pagination;
import com.example.capstone_project.controller.responses.report.list.DepartmentResponse;
import com.example.capstone_project.controller.responses.report.list.ReportResponse;
import com.example.capstone_project.controller.responses.report.list.StatusResponse;
import com.example.capstone_project.controller.responses.report.list.TermResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/report")
@RequiredArgsConstructor
public class ReportController {
    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteReport(
            @Valid @RequestBody DeleteReportBody reportBody
    ) {
        System.out.println(reportBody.toString());
        return null;
    }

    @GetMapping("/list")
    public ResponseEntity<ListResponse<ReportResponse>> getListReport(
            @RequestParam(required = false) Integer termId,
            @RequestParam(required = false) Integer departmentId,
            @RequestParam(required = false) Integer statusId,
            @RequestParam(required = false) String query,
            @RequestParam(required = false) String page,
            @RequestParam(required = false) String size,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String sortType
    ) {
        ListResponse<ReportResponse> listResponse = new ListResponse<>();
        listResponse.setData(List.of(
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

        listResponse.setPagination(Pagination.builder()
                .totalRecords(777)
                .page(10)
                .limitRecordsPerPage(555)
                .numPages(1)
                .build());

        return ResponseEntity.ok(listResponse);
    }
}
