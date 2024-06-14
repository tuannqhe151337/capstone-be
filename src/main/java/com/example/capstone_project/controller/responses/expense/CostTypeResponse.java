package com.example.capstone_project.controller.responses.expense;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class CostTypeResponse {
    private Long costTypeId;
    private String name;
}
