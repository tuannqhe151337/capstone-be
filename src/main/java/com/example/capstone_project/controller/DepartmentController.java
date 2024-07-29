package com.example.capstone_project.controller;

import com.example.capstone_project.controller.responses.ListPaginationResponse;
import com.example.capstone_project.controller.responses.ListResponse;
import com.example.capstone_project.controller.responses.Pagination;
import com.example.capstone_project.controller.responses.department.paginate.DepartmentPaginateResponse;
import com.example.capstone_project.controller.responses.user.DepartmentResponse;
import com.example.capstone_project.entity.Department;
import com.example.capstone_project.service.DepartmentService;
import com.example.capstone_project.utils.exception.UnauthorizedException;
import com.example.capstone_project.utils.helper.PaginationHelper;
import com.example.capstone_project.utils.mapper.user.department.DepartToDepartResponseMapperImpl;

import com.example.capstone_project.utils.mapper.department.paginate.DepartmentPaginateResponseMapperImpl;
import com.example.capstone_project.entity.Department;
import com.example.capstone_project.service.DepartmentService;
import com.example.capstone_project.utils.helper.PaginationHelper;
import com.example.capstone_project.utils.mapper.user.department.DepartToDepartResponseMapperImpl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
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
            List<Department> departments = departmentService.getListDepartmentPaging(query, pageable);

            // Response
            ListPaginationResponse<DepartmentPaginateResponse> response = new ListPaginationResponse<>();

            long count = 0;

            if (departments != null) {

                // Count total record
                count = departmentService.countDistinctListDepartmentPaging(query);

                // Mapping to TermPaginateResponse
                departments.forEach(department -> response.getData().add(new DepartmentPaginateResponseMapperImpl().mapToDepartmentPaginateResponseMapping(department)));

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
        List<Department> departments = departmentService.getListDepartmentPaging(query, pageable);

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
