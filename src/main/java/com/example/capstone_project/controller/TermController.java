package com.example.capstone_project.controller;

import com.example.capstone_project.controller.body.term.create.CreateTermBody;
import com.example.capstone_project.controller.body.term.delete.DeleteTermBody;
import com.example.capstone_project.controller.body.term.update.UpdateTermBody;
import com.example.capstone_project.controller.responses.term.get_plans.PlanStatusResponse;
import com.example.capstone_project.controller.responses.term.get_termDetail.TermDetailResponse;
import com.example.capstone_project.controller.responses.term.get_plans.TermPlanDetailResponse;
import com.example.capstone_project.controller.responses.term.get_termDetail.TermStatusResponse;
import com.example.capstone_project.entity.TermDuration;
import com.example.capstone_project.service.term.TermService;
import com.example.capstone_project.utils.enums.TermCode;
import com.example.capstone_project.utils.helper.PaginationHelper;

import com.example.capstone_project.utils.mapper.term.update.UpdateTermBodyToTermDetailResponseMapperImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.example.capstone_project.controller.responses.ListResponse;
import com.example.capstone_project.controller.responses.Pagination;
import com.example.capstone_project.controller.responses.term.paginate.StatusResponse;
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


    @GetMapping
    public ResponseEntity<ListResponse<TermPlanDetailResponse>> getPlanListByTerm
            (@RequestParam(name = "id") Long termId,
             @RequestParam(defaultValue = "0") int page,
             @RequestParam(defaultValue = "10") int limit) {

        TermPlanDetailResponse termplan =
                TermPlanDetailResponse.builder()
                        .id(1L)
                        .name("PLAN 1")
                        .planStatus(PlanStatusResponse.builder().id(1L).name("REVIEWED").build())
                        .build();
        termplan.setCreatedAt(LocalDateTime.now());
        termplan.setUpdatedAt(LocalDateTime.now());

        TermPlanDetailResponse termplan2 =
                TermPlanDetailResponse.builder()
                        .id(1L)
                        .name("PLAN 2")
                        .planStatus(PlanStatusResponse.builder().id(1L).name("REVIEWED").build())
                        .build();
        termplan2.setCreatedAt(LocalDateTime.now());
        termplan2.setUpdatedAt(LocalDateTime.of(2025, 11, 6, 0, 0, 0));

        TermPlanDetailResponse termplan3 =
                TermPlanDetailResponse.builder()
                        .id(1L)
                        .name("PLAN 3")
                        .planStatus(PlanStatusResponse.builder().id(1L).name("REVIEWED").build())
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

        // Tạo Pageable từ thông tin trang và giới hạn
        PageRequest pageRequest = PageRequest.of(page, limit, Sort.by("updatedAt").descending());

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

        ListResponse<TermPlanDetailResponse> response = new ListResponse<>();
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
    public ResponseEntity<ListResponse<TermPaginateResponse>> getListTermPaging(
            @RequestParam(required = false) String query,
            @RequestParam(required = false) String page,
            @RequestParam(required = false) String size,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String sortType
    ) {
        ListResponse<TermPaginateResponse> listResponse = new ListResponse<>();
        listResponse.setData(List.of(
                TermPaginateResponse.builder()
                        .termId(1L)
                        .name("Term name 1")
                        .status(StatusResponse.builder()
                                .statusId(1L)
                                .name("New").build()
                        )
                        .startDate(LocalDateTime.now())
                        .endDate(LocalDateTime.of(2024, 10, 2, 5, 4, 0)).build(),
                TermPaginateResponse.builder()
                        .termId(2L)
                        .name("Term name 2")
                        .status(StatusResponse.builder()
                                .statusId(2L)
                                .name("Approved").build()
                        )
                        .startDate(LocalDateTime.now())
                        .endDate(LocalDateTime.of(2024, 10, 2, 5, 4, 0)).build(),
                TermPaginateResponse.builder()
                        .termId(3L)
                        .name("Term name 3")
                        .status(StatusResponse.builder()
                                .statusId(3L)
                                .name("Waiting for review").build()
                        )
                        .startDate(LocalDateTime.now())
                        .endDate(LocalDateTime.of(2024, 10, 2, 5, 4, 0)).build(),
                TermPaginateResponse.builder()
                        .termId(4L)
                        .name("Term name 4")
                        .status(StatusResponse.builder()
                                .statusId(1L)
                                .name("Reviewed").build()
                        )
                        .startDate(LocalDateTime.now())
                        .endDate(LocalDateTime.of(2024, 10, 2, 5, 4, 0)).build()
        ));

        listResponse.setPagination(Pagination.builder()
                .totalRecords(100)
                .page(10)
                .limitRecordsPerPage(9)
                .numPages(1)
                .build());

        return ResponseEntity.ok(listResponse);
    }
}
