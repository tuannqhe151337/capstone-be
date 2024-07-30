package com.example.capstone_project.controller.responses.expense.list;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StatusResponse {
    private Long statusId;
    private String code;
}
