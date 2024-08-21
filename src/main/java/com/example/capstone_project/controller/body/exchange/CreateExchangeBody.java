package com.example.capstone_project.controller.body.exchange;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateExchangeBody {
    @NotNull(message = "Month can't be null.")
    private String month;

    @NotNull(message = "Currency id can't be null.")
    private Long currencyId;

    @NotNull(message = "Amount can't be null.")
    private BigDecimal amount;
}
