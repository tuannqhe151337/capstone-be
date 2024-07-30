package com.example.capstone_project.controller;

import com.example.capstone_project.controller.body.department.DeleteDepartmentBody;
import com.example.capstone_project.controller.body.department.NewDepartmentBody;
import com.example.capstone_project.controller.body.department.UpdateDepartmentBody;
import com.example.capstone_project.controller.body.plan.create.NewPlanBody;
import com.example.capstone_project.controller.responses.ExceptionResponse;
import com.example.capstone_project.controller.responses.ListPaginationResponse;
import com.example.capstone_project.controller.responses.ListResponse;
import com.example.capstone_project.controller.responses.Pagination;
import com.example.capstone_project.controller.responses.department.paginate.DepartmentPaginateResponse;
import com.example.capstone_project.controller.responses.user.DepartmentResponse;
import com.example.capstone_project.entity.*;
import com.example.capstone_project.service.DepartmentService;
import com.example.capstone_project.utils.exception.ResourceNotFoundException;
import com.example.capstone_project.utils.exception.UnauthorizedException;
import com.example.capstone_project.utils.exception.term.InvalidDateException;
import com.example.capstone_project.utils.helper.PaginationHelper;
import com.example.capstone_project.utils.helper.UserHelper;
import com.example.capstone_project.utils.mapper.plan.create.CreatePlanMapperImpl;
import com.example.capstone_project.utils.mapper.user.department.DepartToDepartResponseMapperImpl;

import com.example.capstone_project.utils.mapper.department.paginate.DepartmentPaginateResponseMapperImpl;
import com.example.capstone_project.entity.Department;
import com.example.capstone_project.service.DepartmentService;
import com.example.capstone_project.utils.helper.PaginationHelper;
import com.example.capstone_project.utils.mapper.user.department.DepartToDepartResponseMapperImpl;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/department")
@RequiredArgsConstructor
public class DepartmentController {
    private final DepartmentService departmentService;

    @GetMapping("/list")
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

        int pageInt = PaginationHelper.convertPageToInteger(page);
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

    @PostMapping("/create")
    public ResponseEntity<ExceptionResponse> createDepartment(
            @Valid @RequestBody NewDepartmentBody newDepartmentBody, BindingResult bindingResult) throws Exception {
        try {

            // Save plan
            departmentService.createDepartment(newDepartmentBody.getDepartmentName());

            return ResponseEntity.status(HttpStatus.CREATED).body(ExceptionResponse.builder().field("Create").message("Create successful").build());
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ExceptionResponse.builder().field("Error exception").message("Unauthorized").build());
        } catch (DuplicateKeyException | InvalidDateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ExceptionResponse.builder().field("Error exception").message("Duplicate name department").build());
        }
    }

    @DeleteMapping()
    public ResponseEntity<ExceptionResponse> deleteDepartment(
            @Valid @RequestBody DeleteDepartmentBody deleteDepartmentBody, BindingResult bindingResult) throws Exception {
        try {

            // Delete plan
            departmentService.deleteDepartment(deleteDepartmentBody.getDepartmentId());

            return ResponseEntity.status(HttpStatus.CREATED).body(ExceptionResponse.builder().field("Delete").message("Delete successful").build());
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ExceptionResponse.builder().field("Error exception").message("Unauthorized").build());
        } catch (DuplicateKeyException | ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ExceptionResponse.builder().field("Error exception").message("Not found any department have id = " + deleteDepartmentBody.getDepartmentId()).build());
        }
    }

    @PutMapping("/update")
    public ResponseEntity<ExceptionResponse> deleteDepartment(
            @Valid @RequestBody UpdateDepartmentBody updateDepartmentBody, BindingResult bindingResult) throws Exception {
        try {

            // Update plan
            departmentService.updateDepartment(Department.builder().id(updateDepartmentBody.getDepartmentId()).name(updateDepartmentBody.getDepartmentName()).build());

            return ResponseEntity.status(HttpStatus.CREATED).body(ExceptionResponse.builder().field("Update").message("Update successful").build());
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ExceptionResponse.builder().field("Error exception").message("Unauthorized").build());
        } catch (DuplicateKeyException | ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ExceptionResponse.builder().field("Error exception").message("Not found any department have id = " + updateDepartmentBody.getDepartmentId()).build());
        }
    }

}
