package com.example.capstone_project.controller.responses.planDetail;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StatusPlanDetailResponse {
    private long statusId;
    private String name;
}
