package com.example.capstone_project.controller.body.plan.create;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CostTypeBody {
    private long id;
    private String name;
}
