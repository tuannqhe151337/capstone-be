package com.example.capstone_project.utils.enums;

import lombok.Getter;

@Getter
public enum CostTypeCode {
    DIRECT_COSTS("Direct Costs"),
    INDIRECT_COSTS("Indirect Costs"),
    ADMINISTRATION_COSTS("Administration Costs"),
    OPERATING_COSTS("Operating Costs"),
    MAINTENANCE_COSTS("Maintenance Costs"),
    MANUFACTURING_COSTS("Manufacturing costs");

    private final String value;

    CostTypeCode(String value) {
        this.value = value;
    }
}
