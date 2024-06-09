package com.example.capstone_project.controller.body.report.delete;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DeleteReportBody {
    @NotNull(message = "Report Id can't be null")
    private Long reportId;
}
