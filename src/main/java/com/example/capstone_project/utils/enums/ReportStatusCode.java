package com.example.capstone_project.utils.enums;

import lombok.Getter;

@Getter
public enum ReportStatusCode {
    WAITING_FOR_APPROVAL("Waiting for approval"),
    APPROVED("Approved"),
    REVIEWED("Reviewed");

    private final String value;
    ReportStatusCode(String value) {
        this.value = value;
    }
}
