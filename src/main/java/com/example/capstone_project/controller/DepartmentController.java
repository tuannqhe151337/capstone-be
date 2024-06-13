package com.example.capstone_project.controller;

import com.example.capstone_project.controller.responses.ListResponse;
import com.example.capstone_project.controller.responses.Pagination;
import com.example.capstone_project.controller.responses.department.paginate.DepartmentPaginateResponse;
import com.example.capstone_project.controller.responses.term.paginate.TermPaginateResponse;
import com.example.capstone_project.entity.Department;
import com.example.capstone_project.entity.Term;
import com.example.capstone_project.service.DepartmentService;
import com.example.capstone_project.utils.helper.PaginationHelper;
import com.example.capstone_project.utils.mapper.department.paginate.DepartmentPaginateResponseMapper;
import com.example.capstone_project.utils.mapper.department.paginate.DepartmentPaginateResponseMapperImpl;
import com.example.capstone_project.utils.mapper.term.paginate.TermPaginateResponseMapperImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
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
    private final DepartmentService departmentService;

    @GetMapping("/plan-paging-department")
    public ResponseEntity<ListResponse<DepartmentPaginateResponse>> getListDepartmentPaging(
            @RequestParam(required = false) String query,
            @RequestParam(required = false) String page,
            @RequestParam(required = false) String size,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String sortType
    ) {
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
        ListResponse<DepartmentPaginateResponse> response = new ListResponse<>();

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
                .count(count)
                .page(pageInt)
                .displayRecord(sizeInt)
                .numPages(numPages)
                .build());

        return ResponseEntity.ok(response);
//        ListResponse<DepartmentPaginateResponse> listResponse = new ListResponse<>();
//        listResponse.setData(List.of(
//                DepartmentPaginateResponse.builder()
//                        .departmentId(1L)
//                        .name("Department 1")
//                        .build(),
//                DepartmentPaginateResponse.builder()
//                        .departmentId(2L)
//                        .name("Department 2")
//                        .build(),
//                DepartmentPaginateResponse.builder()
//                        .departmentId(3L)
//                        .name("Department 3")
//                        .build(),
//                DepartmentPaginateResponse.builder()
//                        .departmentId(3L)
//                        .name("Department 3")
//                        .build(),
//                DepartmentPaginateResponse.builder()
//                        .departmentId(4L)
//                        .name("Department 4")
//                        .build()
//        ));
//
//        listResponse.setPagination(Pagination.builder()
//                .count(100)
//                .page(10)
//                .displayRecord(0)
//                .numPages(1)
//                .build());
//
//        return ResponseEntity.ok(listResponse);
    }

}
