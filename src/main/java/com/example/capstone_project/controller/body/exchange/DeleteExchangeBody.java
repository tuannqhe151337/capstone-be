package com.example.capstone_project.controller.body.exchange;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DeleteExchangeBody {
    @NotNull(message = "Exchange id can't be empty")
    private Long exchangeId;
}
