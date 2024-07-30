package com.example.capstone_project.controller.body.report.expenses;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReportExpensesBody {
    @NotNull(message = "Report Id need to see list expenses can't be empty")
    private Long reportId;
}