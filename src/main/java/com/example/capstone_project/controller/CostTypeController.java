package com.example.capstone_project.controller;

import com.example.capstone_project.controller.responses.confirm_expenses.CostTypeResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/costType")
@RequiredArgsConstructor
public class CostTypeController {

    @GetMapping("/list")
    public ResponseEntity<List<CostTypeResponse>> confirmExpense() {
        return ResponseEntity.ok(List.of(
                CostTypeResponse.builder()
                        .costTypeId(1L)
                        .name("Direct cost")
                        .build(),
                CostTypeResponse.builder()
                        .costTypeId(2L)
                        .name("Administration cost")
                        .build(),
                CostTypeResponse.builder()
                        .costTypeId(3L)
                        .name("Coty type 3")
                        .build(),
                CostTypeResponse.builder()
                        .costTypeId(4L)
                        .name("Cost type 4")
                        .build()
        ));
    }

}
