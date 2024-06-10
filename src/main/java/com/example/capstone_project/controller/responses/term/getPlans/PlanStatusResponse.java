package com.example.capstone_project.controller.responses.term.getPlans;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class PlanStatusResponse {

    private Long id;

    private String name;

    private String code;
}
