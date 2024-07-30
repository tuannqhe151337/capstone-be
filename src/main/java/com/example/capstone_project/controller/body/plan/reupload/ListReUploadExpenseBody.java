package com.example.capstone_project.controller.body.plan.reupload;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class ListReUploadExpenseBody {
    @NotNull(message = "Plan id can't be null")
    private Long planId;
    @NotNull(message = "List expense need to upload can't be null")
    private List<ReUploadExpenseBody> data = new ArrayList<>();
}