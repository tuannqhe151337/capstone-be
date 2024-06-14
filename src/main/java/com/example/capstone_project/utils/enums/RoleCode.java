package com.example.capstone_project.utils.enums;

import lombok.Getter;

@Getter
public enum RoleCode {
    ADMIN("admin"),ACCOUNTANT("accountant"),FINANCIAL_STAFF("financial-staff");

    private final String value;
    RoleCode(String value) {
        this.value = value;
    }
}
