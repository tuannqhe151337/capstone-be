package com.example.capstone_project.controller;

import com.example.capstone_project.controller.responses.ListPaginationResponse;
import com.example.capstone_project.controller.responses.ListResponse;
import com.example.capstone_project.controller.responses.Pagination;
import com.example.capstone_project.controller.responses.department.paginate.DepartmentPaginateResponse;
import com.example.capstone_project.controller.responses.user.DepartmentResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/department")
@RequiredArgsConstructor
public class DepartmentController {
    @GetMapping("/plan-paging-department")
    public ResponseEntity<ListPaginationResponse<DepartmentPaginateResponse>> getListDepartmentPaging(
            @RequestParam(required = false) String query,
            @RequestParam(required = false) String page,
            @RequestParam(required = false) String size,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String sortType
    ) {
        ListPaginationResponse<DepartmentPaginateResponse> listPaginationResponse = new ListPaginationResponse<>();
        listPaginationResponse.setData(List.of(
                DepartmentPaginateResponse.builder()
                        .departmentId(1L)
                        .name("Department 1")
                        .build(),
                DepartmentPaginateResponse.builder()
                        .departmentId(2L)
                        .name("Department 2")
                        .build(),
                DepartmentPaginateResponse.builder()
                        .departmentId(3L)
                        .name("Department 3")
                        .build(),
                DepartmentPaginateResponse.builder()
                        .departmentId(3L)
                        .name("Department 3")
                        .build(),
                DepartmentPaginateResponse.builder()
                        .departmentId(4L)
                        .name("Department 4")
                        .build()
        ));

        listPaginationResponse.setPagination(Pagination.builder()
                .totalRecords(100)
                .page(10)
                .limitRecordsPerPage(7)
                .numPages(1)
                .build());

        return ResponseEntity.ok(listPaginationResponse);
    }

    @GetMapping("/user-paging-department")
    public ResponseEntity<ListResponse<DepartmentResponse>> getListDepartmentPagingUser() {
        ListResponse<DepartmentResponse> departmentResponseList = new ListResponse<>();
        departmentResponseList.setData(List.of(
                DepartmentResponse.builder()
                        .id(1L)
                        .name("Department 1")
                        .build(),
                DepartmentResponse.builder()
                        .id(2L)
                        .name("Department 2")
                        .build(),
                DepartmentResponse.builder()
                        .id(3L)
                        .name("Department 3")
                        .build(),
                DepartmentResponse.builder()
                        .id(3L)
                        .name("Department 3")
                        .build(),
                DepartmentResponse.builder()
                        .id(4L)
                        .name("Department 4")
                        .build()));
        return ResponseEntity.ok(departmentResponseList);
    }


}
