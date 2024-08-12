package com.example.capstone_project.controller.body.exchange;

import jakarta.validation.constraints.Min;
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
public class ExchangeRateBody {
    @NotNull(message = "Unit price can't be null.")
    private Long currencyId;

    @Min(value = 0, message = "Unit price can't be negative")
    private BigDecimal amount;
}
