package com.example.capstone_project.utils.mapper.report.expenses;

import com.example.capstone_project.controller.responses.expense.CurrencyResponse;
import com.example.capstone_project.controller.responses.report.*;
import com.example.capstone_project.controller.responses.report.expenses.ExpenseResponse;
import com.example.capstone_project.repository.result.ReportExpenseResult;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ReportExpenseResponseMapper {
    default ExpenseResponse mapToReportExpenseResponseMapping(ReportExpenseResult reportExpenseResult) {
        return ExpenseResponse.builder()
                .expenseId(reportExpenseResult.getExpenseId())
                .expenseCode(reportExpenseResult.getExpenseCode())
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
                .project(ProjectResponse.builder()
                        .projectId(reportExpenseResult.getProjectId())
                        .name(reportExpenseResult.getProjectName()).build())
                .supplier(SupplierResponse.builder()
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
                .currency(CurrencyResponse.builder()
                        .currencyId(reportExpenseResult.getCurrency().getId())
                        .name(reportExpenseResult.getCurrency().getName())
                        .symbol(reportExpenseResult.getCurrency().getSymbol())
                        .affix(reportExpenseResult.getCurrency().getAffix())
                        .build())
                .approvedBy(ApprovedByResponse.builder()
                        .approvedById(reportExpenseResult.getApprovedById())
                        .name(reportExpenseResult.getApprovedByName())
                        .build())
                .createdAt(reportExpenseResult.getCreatedAt())
                .updatedAt(reportExpenseResult.getUpdatedAt())
                .build();
    }

}
