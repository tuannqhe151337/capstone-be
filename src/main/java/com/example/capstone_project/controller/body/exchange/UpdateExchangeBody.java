package com.example.capstone_project.controller.body.exchange;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateExchangeBody {
    @NotNull(message = "Exchange id can't be null.")
    private Long exchangeId;

    @NotNull(message = "Amount can't be null.")
    @Min(value = 0, message = "Unit price can't be negative")
    private BigDecimal amount;
}
