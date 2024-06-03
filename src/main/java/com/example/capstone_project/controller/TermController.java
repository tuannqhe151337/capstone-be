package com.example.capstone_project.controller;

import com.example.capstone_project.controller.responses.ListResponse;
import com.example.capstone_project.controller.responses.Pagination;
import com.example.capstone_project.controller.responses.term.paginate.TermPaginate;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/term")
public class TermController {
    @GetMapping("/plan-paging-term")
    public ResponseEntity<ListResponse<TermPaginate>> getListTermPaging(
            @RequestParam(required = false) String page,
            @RequestParam(required = false) String size,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String sortType
    ){
        ListResponse<TermPaginate> listResponse = new ListResponse<>();
        listResponse.setData(List.of(
                TermPaginate.builder()
                        .termId(1L)
                        .name("Term name 1").build(),
                TermPaginate.builder()
                        .termId(2L)
                        .name("Term name 2").build(),
                TermPaginate.builder()
                        .termId(3L)
                        .name("Term name 3").build(),
                TermPaginate.builder()
                        .termId(4L)
                        .name("Term name 4").build(),
                TermPaginate.builder()
                        .termId(5L)
                        .name("Term name 5").build(),
                TermPaginate.builder()
                        .termId(6L)
                        .name("Term name 6").build()
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
