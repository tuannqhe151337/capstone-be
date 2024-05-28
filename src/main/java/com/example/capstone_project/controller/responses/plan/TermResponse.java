package com.example.capstone_project.controller.responses.plan;

import lombok.Builder;
import lombok.Data;

import java.util.List;
@Data
@Builder
public class TermResponse {
    private long termId;
    private String name;
}
