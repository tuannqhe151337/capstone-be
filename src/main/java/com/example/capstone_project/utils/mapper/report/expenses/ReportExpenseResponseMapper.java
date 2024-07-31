package com.example.capstone_project.utils.mapper.report.expenses;

import com.example.capstone_project.controller.responses.report.CostTypeResponse;
import com.example.capstone_project.controller.responses.report.DepartmentResponse;
import com.example.capstone_project.controller.responses.report.StatusResponse;
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
                .projectName(reportExpenseResult.getProjectName())
                .supplierName(reportExpenseResult.getSupplierName())
                .pic(reportExpenseResult.getPic())
                .notes(reportExpenseResult.getNote())
                .status(StatusResponse.builder()
                        .statusId(reportExpenseResult.getStatusId())
                        .name(reportExpenseResult.getStatusName())
                        .build())
                .build();
    }

}
