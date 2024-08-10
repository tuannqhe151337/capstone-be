package com.example.capstone_project.controller.body.exchange;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateExchangeBody {
    @NotNull(message = "Month can't be null.")
    private String month;

    @NotNull(message = "List exchange can't be null.")
    private List<ExchangeBody> exchangeRates;
}


