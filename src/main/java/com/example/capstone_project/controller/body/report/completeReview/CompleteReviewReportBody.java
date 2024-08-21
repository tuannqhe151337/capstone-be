package com.example.capstone_project.controller.body.report.completeReview;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompleteReviewReportBody {
    @NotNull(message = "Report id can't be null")
    private long reportId;
}
