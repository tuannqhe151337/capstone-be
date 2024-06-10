package com.example.capstone_project.utils.enums;

import lombok.Getter;

@Getter
public enum PlanStatusCode {
    NEW("New"),
    WAITING_FOR_REVIEW("Waiting for review"),
    APPROVED("Approved"),
    REVIEWED("Reviewed");

    private final String value;
    PlanStatusCode(String value) {
        this.value = value;
    }
}
