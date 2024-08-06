package com.example.capstone_project.utils.mapper.report.expenses;

import com.example.capstone_project.controller.responses.report.*;
import com.example.capstone_project.controller.responses.report.expenses.ExpenseResponse;
import com.example.capstone_project.repository.result.ReportExpenseResult;
import org.mapstruct.Mapper;

import java.math.BigDecimal;

@Mapper(componentModel = "spring")
public interface ReportExpenseResponseMapper {
    default ExpenseResponse mapToReportExpenseResponseMapping(ReportExpenseResult reportExpenseResult) {
        return ExpenseResponse.builder()
                .expenseId(reportExpenseResult.getExpenseId())
                .department(DepartmentResponse.builder()
                        .departmentId(reportExpenseResult.getDepartmentId())
                        .name(reportExpenseResult.getDepartmentName())
                        .build())
                .name(reportExpenseResult.getExpenseName())
                .costType(CostTypeResponse.builder()
                        .costTypeId(reportExpenseResult.getCostTypeId())
                        .name(reportExpenseResult.getCostTypeName())
                        .build())
                .unitPrice(reportExpenseResult.getUnitPrice())
                .amount(reportExpenseResult.getAmount())
                .projectName(ProjectResponse.builder()
                        .projectId(reportExpenseResult.getProjectId())
                        .name(reportExpenseResult.getProjectName()).build())
                .supplierName(SupplierResponse.builder()
                        .supplierId(reportExpenseResult.getSupplierId())
                        .name(reportExpenseResult.getSupplierName()).build())
                .pic(PicResponse.builder()
                        .picId(reportExpenseResult.getPicId())
                        .name(reportExpenseResult.getPicName()).build())
                .notes(reportExpenseResult.getNote())
                .status(StatusResponse.builder()
                        .statusId(reportExpenseResult.getStatusId())
                        .code(reportExpenseResult.getStatusCode())
                        .name(reportExpenseResult.getStatusName())
                        .build())
                .build();
    }

}
