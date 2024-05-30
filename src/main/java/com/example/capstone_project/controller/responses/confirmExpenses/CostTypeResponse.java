package com.example.capstone_project.controller.responses.confirmExpenses;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class CostTypeResponse {
    private long costTypeId;
    private String name;
}
