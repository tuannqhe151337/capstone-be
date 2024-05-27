package com.example.capstone_project.controller;

import com.example.capstone_project.controller.responses.ListResponse;
import com.example.capstone_project.controller.responses.Pagination;
import com.example.capstone_project.controller.responses.plan.DepartmentResponse;
import com.example.capstone_project.controller.responses.plan.PlanResponse;
import com.example.capstone_project.controller.responses.plan.StatusResponse;
import com.example.capstone_project.controller.responses.plan.TermResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/list-plan")
@RequiredArgsConstructor
public class FinancialPlanController {
    @GetMapping
    public ResponseEntity<ListResponse<TermResponse>> create(){
        ListResponse<TermResponse> listResponse = new ListResponse<>();
        listResponse.setData(
                List.of(
                TermResponse.builder()
                        .termId(1L)
                        .name("Term")
                        .plans(
                                List.of(
                                        PlanResponse.builder()
                                                .id(1L)
                                                .name("Plan 1")
                                                .department(
                                                        DepartmentResponse.builder()
                                                                .departmentId(1L)
                                                                .name("Department name 1")
                                                                .build()
                                                )
                                                .version("Version Mock")
                                                .status(
                                                        StatusResponse.builder()
                                                                .statusId(1L)
                                                                .name("Status name").build()
                                                ).build(),
                                        PlanResponse.builder()
                                                .id(2L)
                                                .name("Plan 2")
                                                .department(
                                                        DepartmentResponse.builder()
                                                                .departmentId(1L)
                                                                .name("Department name 2")
                                                                .build()
                                                )
                                                .version("Version Mock")
                                                .status(
                                                        StatusResponse.builder()
                                                                .statusId(1L)
                                                                .name("Status name").build()
                                                ).build()
                                )
                        )
                        .build(),
                        TermResponse.builder()
                                .termId(2L)
                                .name("Term")
                                .plans(
                                        List.of(
                                                PlanResponse.builder()
                                                        .id(3L)
                                                        .name("Plan 3")
                                                        .department(
                                                                DepartmentResponse.builder()
                                                                        .departmentId(1L)
                                                                        .name("Department name 1")
                                                                        .build()
                                                        )
                                                        .version("Version Mock")
                                                        .status(
                                                                StatusResponse.builder()
                                                                        .statusId(1L)
                                                                        .name("Status name").build()
                                                        ).build(),
                                                PlanResponse.builder()
                                                        .id(4L)
                                                        .name("Plan 4")
                                                        .department(
                                                                DepartmentResponse.builder()
                                                                        .departmentId(1L)
                                                                        .name("Department name 2")
                                                                        .build()
                                                        )
                                                        .version("Version Mock")
                                                        .status(
                                                                StatusResponse.builder()
                                                                        .statusId(1L)
                                                                        .name("Status name").build()
                                                        ).build()
                                        )
                                )
                                .build()
        ));

        listResponse.setPagination(Pagination.builder()
                .count(100)
                .page(10)
                .displayRecord(0)
                .numPages(1)
                .build());

        return ResponseEntity.ok(listResponse);
    };
}
