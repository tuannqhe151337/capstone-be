package com.example.capstone_project.utils.enums;

import lombok.Getter;

@Getter
public enum ExpenseStatusCode {
    NEW("new"),
    WAITING_FOR_APPROVAL("waiting-for-approval"),
    APPROVED("approved"),
    DENIED("denied");

    private final String value;
    ExpenseStatusCode(String value) {
        this.value = value;
    }
}
