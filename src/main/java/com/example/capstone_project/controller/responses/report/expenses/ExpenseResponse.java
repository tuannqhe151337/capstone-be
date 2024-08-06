package com.example.capstone_project.controller.responses.report.expenses;

import com.example.capstone_project.controller.responses.report.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ExpenseResponse {
    private Long expenseId;
    private DepartmentResponse department;
    private String name;
    private CostTypeResponse costType;
    private BigDecimal unitPrice;
    private Integer amount;
    private ProjectResponse projectName;
    private SupplierResponse supplierName;
    private PicResponse pic;
    private String notes;
    private StatusResponse status;
}
