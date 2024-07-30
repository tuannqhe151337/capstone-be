package com.example.capstone_project.controller;

import com.example.capstone_project.controller.responses.ListPaginationResponse;
import com.example.capstone_project.controller.responses.Pagination;
import com.example.capstone_project.controller.responses.department.paginate.DepartmentPaginateResponse;
import com.example.capstone_project.controller.responses.user.DepartmentResponse;
import com.example.capstone_project.entity.Department;
import com.example.capstone_project.service.DepartmentService;
import com.example.capstone_project.utils.helper.PaginationHelper;
import com.example.capstone_project.utils.mapper.user.department.DepartToDepartResponseMapperImpl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
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
    private final DepartmentService departmentService;


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
    public ResponseEntity<ListPaginationResponse<DepartmentResponse>> getListDepartmentPagingUser(
            @RequestParam(required = false) String query,
            @RequestParam(required = false) String page,
            @RequestParam(required = false) String size,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String sortType
    ) {

        int pageInt = PaginationHelper.convertPageToInteger(page) ;
        int sizeInt = PaginationHelper.convertPageSizeToInteger(size);

        // Handling query
        if (query == null) {
            query = "";
        }

        // Handling pagination
        Pageable pageable = PaginationHelper.handlingPagination(pageInt, sizeInt, sortBy, sortType);

        // Get data
        List<Department> departments = departmentService.getDepartmentWithPagination(query, pageable);

        //map
        List<DepartmentResponse> departmentResponses =
               new DepartToDepartResponseMapperImpl()
                        .mapDepartmentsToDepartmentResponses(departments);
        //count, totalrecords
        long totalRecords = departmentService.countDistinct(query);
        long numPages = PaginationHelper.calculateNumPages(totalRecords, sizeInt);

        // Response
        ListPaginationResponse<DepartmentResponse> response = new ListPaginationResponse<>();

        response.setData(departmentResponses);


        response.setPagination(Pagination.builder()
                .totalRecords(totalRecords)
                .page(pageInt)
                .limitRecordsPerPage(sizeInt)
                .numPages(numPages)
                .build());

        return ResponseEntity.ok(response);
    }


}
