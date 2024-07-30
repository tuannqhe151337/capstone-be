package com.example.capstone_project.repository.result;

import com.example.capstone_project.utils.enums.DepartmentCode;

import java.time.LocalDateTime;

public interface PlanVersionResult {
    Long getPlanId();
    Integer getVersion();
    String getTermName();
    DepartmentCode getDepartmentCode();
}
