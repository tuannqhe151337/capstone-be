package com.example.capstone_project.utils.mapper.plan.status;

import com.example.capstone_project.controller.responses.plan.StatusResponse;
import com.example.capstone_project.entity.ExpenseStatus;
import com.example.capstone_project.entity.ReportStatus;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PlanStatusMapper {

    @Mapping(source = "id", target = "statusId")
    @Mapping(source = "code.value", target = "code")
    StatusResponse mapToStatusResponseMapping(ReportStatus reportStatus);

    @Mapping(source = "id", target = "statusId")
    @Mapping(source = "code.value", target = "code")
    StatusResponse mapExpenseStatusToStatusResponseMapping(ExpenseStatus expenseStatus);
}
