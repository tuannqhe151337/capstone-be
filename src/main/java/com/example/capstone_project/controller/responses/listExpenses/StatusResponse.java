package com.example.capstone_project.controller.responses.listExpenses;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StatusResponse {
    private long statusId;
    private String name;
}
