package com.example.capstone_project.controller;


import com.example.capstone_project.controller.responses.ListResponse;
import com.example.capstone_project.controller.responses.term.getTermStatus.TermStatusResponse;
import com.example.capstone_project.controller.responses.user.PositionResponse;
import com.example.capstone_project.utils.enums.TermCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/term-status")
@Validated
public class TermStatusController {

    @GetMapping("/term-status-filter")
    public ResponseEntity<List<TermStatusResponse>> getListTermStatusFilter(){
        ListResponse<TermStatusResponse> listResponse = new ListResponse<>();
        List<TermStatusResponse> termlist = new ArrayList<>();
        termlist = List.of(
                TermStatusResponse.builder()
                        .id(1L)
                        .name("CLOSED")
                        .code(TermCode.CLOSED)
                        .build(),
                TermStatusResponse.builder()
                        .id(2L)
                        .name("IN_PROGRESS")
                        .code(TermCode.IN_PROGRESS)
                        .build(),
                TermStatusResponse.builder()
                        .id(3L)
                        .name("NOT_STARTED")
                        .code(TermCode.NOT_STARTED)
                        .build()
        );

        return ResponseEntity.ok(termlist);
    }
}
