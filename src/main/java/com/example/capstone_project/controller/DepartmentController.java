package com.example.capstone_project.controller;

import com.example.capstone_project.controller.responses.ListResponse;
import com.example.capstone_project.controller.responses.Pagination;
import com.example.capstone_project.controller.responses.department.paginate.DepartmentPaginateResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/department")
@RequiredArgsConstructor
public class DepartmentController {
    @GetMapping("/plan-paging-department")
    public ResponseEntity<ListResponse<DepartmentPaginateResponse>> getListDepartmentPaging(
            @RequestParam(required = false) String page,
            @RequestParam(required = false) String size,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String sortType
    ){
        ListResponse<DepartmentPaginateResponse> listResponse = new ListResponse<>();
        listResponse.setData(List.of(
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

        listResponse.setPagination(Pagination.builder()
                .count(100)
                .page(10)
                .displayRecord(0)
                .numPages(1)
                .build());

        return ResponseEntity.ok(listResponse);
    }

}
