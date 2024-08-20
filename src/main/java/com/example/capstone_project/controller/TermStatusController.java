package com.example.capstone_project.controller;


import com.example.capstone_project.controller.responses.ListResponse;
import com.example.capstone_project.controller.responses.term.getStatus.TermStatusResponse;
import com.example.capstone_project.service.TermStatusService;
import com.example.capstone_project.utils.mapper.termStatus.TermStatusToTermStatusResponseMapperImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/term-status")

public class TermStatusController {
    private final TermStatusService termStatusService;

    @GetMapping("/term-status-list")
    public ResponseEntity<ListResponse<TermStatusResponse>> getListTermStatusFilter() {
        ListResponse<TermStatusResponse> listResponse = new ListResponse<>();
        List<TermStatusResponse> termlist =
                new TermStatusToTermStatusResponseMapperImpl()
                        .mmapTermStatusToTermStatusResponseList(termStatusService.getTermStatuses());
        listResponse.setData(termlist);

        return ResponseEntity.ok(listResponse);
    }
}
