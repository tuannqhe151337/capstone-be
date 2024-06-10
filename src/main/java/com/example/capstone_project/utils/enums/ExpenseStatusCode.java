package com.example.capstone_project.utils.enums;

import lombok.Getter;

@Getter
public enum ExpenseStatusCode {
    NEW("New"),WAITING_FOR_APPROVAL("Waiting for approval"),APPROVED("Approved"),DENIED("Denied");
    private final String value;
    ExpenseStatusCode(String value) {
        this.value = value;
    }
}
