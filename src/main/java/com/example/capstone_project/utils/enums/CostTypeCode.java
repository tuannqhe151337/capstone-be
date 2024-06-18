package com.example.capstone_project.utils.enums;

import lombok.Getter;

@Getter
public enum CostTypeCode {
    DIRECT_COSTS("direct-costs"),
    INDIRECT_COSTS("indirect-costs"),
    ADMINISTRATION_COSTS("administration-costs"),
    OPERATING_COSTS("operating-costs"),
    MAINTENANCE_COSTS("maintenance-costs"),
    MANUFACTURING_COSTS("manufacturing-costs");

    private final String value;

    CostTypeCode(String value) {
        this.value = value;
    }
}
