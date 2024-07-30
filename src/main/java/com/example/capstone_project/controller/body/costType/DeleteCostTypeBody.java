package com.example.capstone_project.controller.body.costType;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DeleteCostTypeBody {
    @NotNull(message = "Cost type id can't be empty")
    private Long costTypeId;
}
