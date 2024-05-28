package com.example.capstone_project.controller;

import com.example.capstone_project.controller.responses.ListResponse;
import com.example.capstone_project.controller.responses.Pagination;
import com.example.capstone_project.controller.responses.select_term.TermResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/select-term")
@RequiredArgsConstructor
public class TermController {
    @GetMapping
    public ResponseEntity<ListResponse<TermResponse>> selectTerm(
            @RequestParam(required = false) String query,
            @RequestParam(required = false) String page,
            @RequestParam(required = false) String size,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String sortType
    ) {
        ListResponse<TermResponse> listResponse = new ListResponse<>();
        listResponse.setData(List.of(
                TermResponse.builder()
                        .termId(1L)
                        .name("Term 1")
                        .duration("Quarterly")
                        .startDate(LocalDate.of(2024, 6, 23))
                        .endDate(LocalDate.of(2024, 7, 21))
                        .build(),
                TermResponse.builder()
                        .termId(1L)
                        .name("Term 1")
                        .duration("Quarterly")
                        .startDate(LocalDate.of(2024, 6, 23))
                        .endDate(LocalDate.of(2024, 7, 21))
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
