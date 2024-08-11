package com.example.capstone_project.controller.responses.expense;

import com.example.capstone_project.utils.enums.Affix;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CurrencyResponse {
    private Long currencyId;
    private String name;
    private String symbol;
    private Affix affix;
}
