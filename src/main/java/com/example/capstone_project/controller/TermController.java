package com.example.capstone_project.controller;

import com.example.capstone_project.controller.body.term.create.CreateTermBody;
import com.example.capstone_project.controller.body.term.delete.DeleteTermBody;
import com.example.capstone_project.controller.body.term.update.UpdateTermBody;
import com.example.capstone_project.controller.responses.term.getPlans.PlanStatusResponse;
import com.example.capstone_project.controller.responses.term.getPlans.TermPlanDetailResponse;
import com.example.capstone_project.controller.responses.term.getReports.TermReportResponse;
import com.example.capstone_project.controller.responses.term.getTermDetail.TermDetailResponse;
import com.example.capstone_project.controller.responses.term.getTermDetail.TermStatusResponse;
import com.example.capstone_project.controller.responses.term.paginate.StatusResponse;
import com.example.capstone_project.controller.responses.term.selectWhenCreatePlan.TermWhenCreatePlanResponse;
import com.example.capstone_project.entity.Term;
import com.example.capstone_project.utils.enums.TermDuration;
import com.example.capstone_project.service.TermService;
import com.example.capstone_project.utils.enums.TermCode;
import com.example.capstone_project.utils.helper.PaginationHelper;
import com.example.capstone_project.utils.mapper.term.paginate.TermPaginateResponseMapper;
import com.example.capstone_project.utils.mapper.term.paginate.TermPaginateResponseMapperImpl;
import com.example.capstone_project.utils.mapper.term.selectWhenCreatePlan.TermWhenCreatePlanMapperImpl;
import com.example.capstone_project.utils.helper.PaginationHelper;
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/term")
@Validated
public class TermController {
    private final TermService termService;


    @GetMapping("/report")
    public ResponseEntity<ListPaginationResponse<TermReportResponse>> getReportListByTerm
            (@RequestParam(name = "termId") Long termId,
             @RequestParam(defaultValue = "1") int page,
             @RequestParam(defaultValue = "10") int size,
             @RequestParam(required = false) String sortBy,
             @RequestParam(required = false) String sortType) {
        TermReportResponse rp1 = TermReportResponse.builder()
                .id(1L).createdAt(LocalDateTime.now()).name("REPORT 1 TERM SPRING").build();
        TermReportResponse rp2 = TermReportResponse.builder()
                .id(2L).createdAt(LocalDateTime.now()).name("REPORT 2 TERM WINTER").build();
        TermReportResponse rp3 = TermReportResponse.builder()
                .id(3L).createdAt(LocalDateTime.now()).name("REPORT 3 TERM FALL").build();
        List<TermReportResponse> rps = new ArrayList<>();
        rps.add(rp1);
        rps.add(rp2);
        rps.add(rp3);
        // Sort the list by updatedAt, from newest to oldest
        Collections.sort(rps, new Comparator<TermReportResponse>() {
            @Override
            public int compare(TermReportResponse o1, TermReportResponse o2) {
                return o2.getCreatedAt().compareTo(o1.getCreatedAt());
            }
        });

        PageRequest pageRequest = (PageRequest) PaginationHelper.handlingPagination(page, size, sortBy, sortType);

        //Tao Page tu list
        Page<TermReportResponse> listTermReport = PaginationHelper.createPage(rps, pageRequest);

        //Build response
        Pagination pagination = Pagination
                .builder()
                .page(pageRequest.getPageNumber())
                .limitRecordsPerPage(pageRequest.getPageSize())
                .totalRecords((long) listTermReport.getNumberOfElements())
                .numPages(PaginationHelper.
                        calculateNumPages((long) listTermReport.getNumberOfElements(),
                                pageRequest.getPageSize())).build();

        ListPaginationResponse<TermReportResponse> response = new ListPaginationResponse<>();
        response.setData(rps);
        response.setPagination(pagination);


        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


    @GetMapping("/plan")
    public ResponseEntity<ListPaginationResponse<TermPlanDetailResponse>> getPlanListByTerm
            (@RequestParam(name = "termId") Long termId,
             @RequestParam(defaultValue = "1") int page,
             @RequestParam(defaultValue = "10") int size,
             @RequestParam(required = false) String sortBy,
             @RequestParam(required = false) String sortType) {

        TermPlanDetailResponse termplan =
                TermPlanDetailResponse.builder()
                        .id(1L)
                        .name("PLAN 1")
                        .planStatus(PlanStatusResponse.builder().id(1L).code("REVIEWED").name("REVIEWED").build())
                        .build();
        termplan.setCreatedAt(LocalDateTime.now());
        termplan.setUpdatedAt(LocalDateTime.now());

        TermPlanDetailResponse termplan2 =
                TermPlanDetailResponse.builder()
                        .id(1L)
                        .name("PLAN 2")
                        .planStatus(PlanStatusResponse.builder().id(1L).code("REVIEWED").name("REVIEWED").build())
                        .build();
        termplan2.setCreatedAt(LocalDateTime.now());
        termplan2.setUpdatedAt(LocalDateTime.of(2025, 11, 6, 0, 0, 0));

        TermPlanDetailResponse termplan3 =
                TermPlanDetailResponse.builder()
                        .id(1L)
                        .name("PLAN 3")
                        .planStatus(PlanStatusResponse.builder().id(1L).code("REVIEWED").name("REVIEWED").build())
                        .build();
        termplan3.setCreatedAt(LocalDateTime.now());
        termplan3.setUpdatedAt(LocalDateTime.of(2026, 11, 6, 0, 0, 0));

        List<TermPlanDetailResponse> list = new ArrayList<>();
        list.add(termplan);
        list.add(termplan2);
        list.add(termplan3);

        // Sort the list by updatedAt, from newest to oldest
        Collections.sort(list, new Comparator<TermPlanDetailResponse>() {
            @Override
            public int compare(TermPlanDetailResponse o1, TermPlanDetailResponse o2) {
                return o2.getUpdatedAt().compareTo(o1.getUpdatedAt());
            }
        });

        PageRequest pageRequest = (PageRequest) PaginationHelper.handlingPagination(page, size, sortBy, sortType);

        //Tao Page tu list
        Page<TermPlanDetailResponse> listTermPlan = PaginationHelper.createPage(list, pageRequest);


        //Build response
        Pagination pagination = Pagination
                .builder()
                .page(pageRequest.getPageNumber())
                .limitRecordsPerPage(pageRequest.getPageSize())
                .totalRecords((long) listTermPlan.getNumberOfElements())
                .numPages(PaginationHelper.
                        calculateNumPages((long) listTermPlan.getNumberOfElements(),
                                pageRequest.getPageSize())).build();

        ListPaginationResponse<TermPlanDetailResponse> response = new ListPaginationResponse<>();
        response.setData(list);
        response.setPagination(pagination);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


    @GetMapping("/{id}")
    public ResponseEntity<TermDetailResponse> getTermDetailmById(@PathVariable("id") Long id) {
        TermDetailResponse termDetailResponse
                = TermDetailResponse.builder()
                .id(1L)
                .name("TERM APRIL 2024")
                .duration(TermDuration.MONTHLY)
                .startDate(LocalDateTime.now())
                .planDueDate(LocalDateTime.now())
                .endDate(TermDuration.MONTHLY.calculateEndDate(LocalDateTime.now()))
                .status(TermStatusResponse.builder()
                        .name("IN_PROGRESS")
                        .isDelete(false)
                        .code(TermCode.IN_PROGRESS).build())
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(termDetailResponse);
    }

    @PostMapping
    public ResponseEntity<String> createTerm(@Valid @RequestBody CreateTermBody createTermBody) {
        return ResponseEntity.status(HttpStatus.CREATED).body("Created successfully");
    }

    @PutMapping
    public ResponseEntity<String> updateTerm(@Valid @RequestBody UpdateTermBody updateTermBody, BindingResult result) {
      TermDetailResponse termDetailResponse = new UpdateTermBodyToTermDetailResponseMapperImpl().mapDeleteTermBodyToDetail(updateTermBody);
        return ResponseEntity.status(HttpStatus.OK).body("Updated successfully");
    }

    @DeleteMapping
    public ResponseEntity<String> deleteTerm(@Valid @RequestBody DeleteTermBody deleteTermBody, BindingResult result) {
        return ResponseEntity.status(HttpStatus.CREATED).body("Deleted successfully");
    }

    @GetMapping("/plan-paging-term")
    public ResponseEntity<ListPaginationResponse<TermPaginateResponse>> getListTermPaging(
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
        List<Term> terms = termService.getListTermPaging(query, pageable);

        // Response
        ListPaginationResponse<TermPaginateResponse> response = new ListPaginationResponse<>();

        long count = 0;

        if (terms != null) {

            // Count total record
            count = termService.countDistinctListTermPaging(query);

            // Mapping to TermPaginateResponse
            terms.forEach(term -> response.getData().add( new TermPaginateResponseMapperImpl().mapToTermPaginateResponseMapper(term)));

        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        System.out.println("id = " + terms.get(0).getId());
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
                .numPages(sizeInt)
                .numPages(numPages)
                .build());

        return ResponseEntity.ok(response);
    }
}
