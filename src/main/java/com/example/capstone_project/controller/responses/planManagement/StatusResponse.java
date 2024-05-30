package com.example.capstone_project.controller.responses.planManagement;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StatusResponse {
    private long statusId;
    private String name;
}
