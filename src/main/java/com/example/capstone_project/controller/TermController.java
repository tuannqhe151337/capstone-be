package com.example.capstone_project.controller;

import com.example.capstone_project.controller.body.term.create.CreateTermBody;
import com.example.capstone_project.controller.body.term.create.UpdateTermBody;
import com.example.capstone_project.controller.responses.term.get.TermDetailResponse;
import com.example.capstone_project.controller.responses.term.get.TermStatusResponse;
import com.example.capstone_project.entity.Term;
import com.example.capstone_project.entity.TermDuration;
import com.example.capstone_project.entity.TermStatus;
import com.example.capstone_project.service.TermService;

import com.example.capstone_project.utils.enums.TermCode;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;


@RequiredArgsConstructor
@RestController
@RequestMapping("/api/term")
public class TermController {
    private final TermService termService;

    @GetMapping
    public ResponseEntity<List<Term>> getAllTerms() {
        return null;
    }

    @GetMapping("/{id}")
    public ResponseEntity<TermDetailResponse> getTermDetailmById(@PathVariable("id") Long id) {
        TermDetailResponse termDetailResponse
                = TermDetailResponse.builder()
                .id(1)
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
    public ResponseEntity<String> updateTerm(@Valid @RequestBody UpdateTermBody updateTermBody) {
        // return .save(term);
    return ResponseEntity.status(HttpStatus.OK).body("Updated successfully");
    }

}
