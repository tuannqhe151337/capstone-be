package com.example.capstone_project.controller.body.report.delete;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeleteReportBody {
    @NotNull(message = "Report id can't be null")
    private Long reportId;
}
