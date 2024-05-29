package com.example.capstone_project.controller.responses.listExpenses;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CostTypeResponse {
    private long costTypeId;
    private String name;
}
