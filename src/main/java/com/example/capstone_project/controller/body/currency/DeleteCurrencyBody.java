package com.example.capstone_project.controller.body.currency;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DeleteCurrencyBody {
    @NotNull(message = "Project id can't be empty")
    private Long currencyId;
}
