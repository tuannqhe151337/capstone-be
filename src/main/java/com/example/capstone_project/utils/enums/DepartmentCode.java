package com.example.capstone_project.utils.enums;

import lombok.Getter;

@Getter
public enum DepartmentCode {
    IT("it"),
    HR("hr"),
    FINANCE("finance"),
    COMMUNICATION("communication"),
    MARKETING("marketing"),
    ACCOUNTING("accounting");

    private final String value;
    DepartmentCode(String value) {
        this.value = value;
    }
}
