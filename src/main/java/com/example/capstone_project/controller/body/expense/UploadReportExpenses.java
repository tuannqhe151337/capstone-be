package com.example.capstone_project.controller.body.expense;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UploadReportExpenses {
    @NotNull(message = "Plan id can not be null")
    private Long reportId;
    @NotNull(message = "List expense id can not be null")
    private List<ExpenseBody> listExpenses;
}
