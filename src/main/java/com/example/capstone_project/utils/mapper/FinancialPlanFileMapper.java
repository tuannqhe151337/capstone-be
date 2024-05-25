package com.example.capstone_project.utils.mapper;

import com.example.capstone_project.controller.responses.FinancialPlanFileResponse;
import com.example.capstone_project.entity.FinancialPlanFile;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface FinancialPlanFileMapper {
    FinancialPlanFileResponse mapToFinancialPlanFileResponse(FinancialPlanFile financialPlanFile);
    FinancialPlanFile mapToFinancialPlanFile(FinancialPlanFileResponse financialPlanFileResponse );
}
