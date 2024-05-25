package com.example.capstone_project.utils.mapper;

import com.example.capstone_project.controller.responses.FinancialStatusResponse;
import com.example.capstone_project.entity.FinancialStatus;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface FinancialStatusMapper {
    FinancialStatusResponse mapToFinancialStatusResponse(FinancialStatus financialStatus);
    FinancialStatus mapToFinancialStatus(FinancialStatusResponse financialStatusResponse);
}
