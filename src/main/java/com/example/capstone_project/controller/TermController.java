package com.example.capstone_project.controller;

import com.example.capstone_project.controller.body.plan.start.StartTermBody;
import com.example.capstone_project.controller.body.term.create.CreateTermBody;
import com.example.capstone_project.controller.body.term.delete.DeleteTermBody;
import com.example.capstone_project.controller.body.term.update.UpdateTermBody;
import com.example.capstone_project.controller.responses.ExceptionResponse;
import com.example.capstone_project.controller.responses.term.getPlans.PlanStatusResponse;
import com.example.capstone_project.controller.responses.term.getPlans.TermPlanDetailResponse;
import com.example.capstone_project.controller.responses.term.getReports.TermReportResponse;
import com.example.capstone_project.controller.responses.term.getTermDetail.TermDetailResponse;

import com.example.capstone_project.controller.responses.term.selectWhenCreatePlan.TermWhenCreatePlanResponse;
import com.example.capstone_project.entity.Term;

import com.example.capstone_project.service.TermService;
import com.example.capstone_project.utils.exception.UnauthorizedException;
import com.example.capstone_project.utils.exception.term.InvalidDateException;

import com.example.capstone_project.utils.exception.ResourceNotFoundException;

import com.example.capstone_project.utils.helper.PaginationHelper;
import com.example.capstone_project.utils.mapper.term.create.CreateTermBodyToTermEntityMapperImpl;
import com.example.capstone_project.utils.mapper.term.detail.TermToTermDetailResponseMapperImpl;
import com.example.capstone_project.utils.mapper.term.paginate.TermPaginateResponseMapperImpl;
import com.example.capstone_project.utils.mapper.term.selectWhenCreatePlan.TermWhenCreatePlanMapperImpl;

import com.example.capstone_project.utils.mapper.term.update.UpdateTermBodyToTermDetailResponseMapperImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.example.capstone_project.controller.responses.ListPaginationResponse;
import com.example.capstone_project.controller.responses.Pagination;
import com.example.capstone_project.controller.responses.term.paginate.TermPaginateResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/term")
@Validated
public class TermController {
    private final TermService termService;


    @GetMapping("/{id}")
    public ResponseEntity<TermDetailResponse> getTermDetailById(@PathVariable("id") Long id) {
        try {
            TermDetailResponse termDetailResponse = new TermToTermDetailResponseMapperImpl()
                    .mapTermToTermDetailResponse(termService.findTermById(id));
            return ResponseEntity.status(HttpStatus.OK).body(termDetailResponse);
        } catch (UnauthorizedException e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping
    public ResponseEntity<Object> createTerm(@Valid @RequestBody CreateTermBody createTermBody) {
        //map create term body to term entity
        Term term = new CreateTermBodyToTermEntityMapperImpl().mapCreateTermBodyToTermEntity(createTermBody);
        try {
            termService.createTerm(term);
            return ResponseEntity.status(HttpStatus.CREATED).body(null);
        } catch (UnauthorizedException e) {
            ExceptionResponse exceptionResponse = ExceptionResponse
                    .builder().field("Authorization").message(e.getMessage())
                    .build();
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(exceptionResponse);
        } catch (InvalidDateException e) {
            ExceptionResponse exceptionResponse = ExceptionResponse
                    .builder().field("PlanDueDate").message("Plan due date must be within 5 days after end date.")
                    .build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exceptionResponse);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }

    }

    @PutMapping
    public ResponseEntity<TermDetailResponse> updateTerm(@Valid @RequestBody UpdateTermBody updateTermBody, BindingResult result) {

        try {
            Term term = new UpdateTermBodyToTermDetailResponseMapperImpl().mapTermBodyToTermEntity(updateTermBody);

            TermDetailResponse termDetailResponse =
                    new UpdateTermBodyToTermDetailResponseMapperImpl()
                            .mapTermToTermDetailResponse(termService.updateTerm(term));

            return ResponseEntity.status(HttpStatus.OK).body(termDetailResponse);
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        } catch (InvalidDateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @DeleteMapping
    public ResponseEntity<String> deleteTerm(@Valid @RequestBody DeleteTermBody deleteTermBody, BindingResult result) {
        try {
            termService.deleteTerm(deleteTermBody.getId());
            return ResponseEntity.status(HttpStatus.OK).body(null);
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Term not found");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal server error");
        }
    }

    @GetMapping("/plan-paging-term")
    public ResponseEntity<ListPaginationResponse<TermPaginateResponse>> getListTermPaging(
            @RequestParam(required = false) Long statusId,
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
        List<Term> terms = termService.getListTermPaging(statusId, query, pageable);

        // Response
        ListPaginationResponse<TermPaginateResponse> response = new ListPaginationResponse<>();

        long count = 0;

        if (terms != null) {

            // Count total record
            count = termService.countDistinctListTermPaging(statusId, query);

            // Mapping to TermPaginateResponse
            terms.forEach(term -> response.getData().add( new TermPaginateResponseMapperImpl().mapToTermPaginateResponseMapper(term)));

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
    }


    @GetMapping("/plan-create-select-term")
    public ResponseEntity<ListPaginationResponse<TermWhenCreatePlanResponse>> getListTermWhenCreatePlan(
            @RequestParam(required = false) Long statusId,
            @RequestParam(required = false) String query,
            @RequestParam(required = false) String page,
            @RequestParam(required = false) String size,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String sortType
    ) throws Exception {
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
        List<Term> terms = termService.getListTermWhenCreatePlan(query, pageable);

        // Response
        ListPaginationResponse<TermWhenCreatePlanResponse> response = new ListPaginationResponse<>();

        long count = 0;

        if (terms != null) {

            // Count total record
            count = termService.countDistinctListTermWhenCreatePlan(query);

            // Mapping to TermPaginateResponse
            terms.forEach(term -> response.getData().add(new TermWhenCreatePlanMapperImpl().mapToTermWhenCreatePlanResponseMapper(term)));

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
    }

    @PostMapping("/start")
    public ResponseEntity<Object> startTermManually(@Valid @RequestBody StartTermBody startTermBody, BindingResult result) {
        try {
            termService.startTermManually(startTermBody.getTermId());
            return ResponseEntity.status(HttpStatus.OK).body(null);
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
