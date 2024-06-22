package com.example.capstone_project.utils.enums;

import lombok.Getter;

@Getter
public enum PlanStatusCode {
    NEW("new"),
    WAITING_FOR_REVIEWED("waiting-for-reviewed"),
    APPROVED("approved"),
    REVIEWED("reviewed");

    private final String value;
    PlanStatusCode(String value) {
        this.value = value;
    }
}
