package com.example.capstone_project.controller.responses;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class CustomSort {
    private String sortBy;
    private String sortType;
}
