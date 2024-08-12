package com.example.capstone_project.service.result;

import com.example.capstone_project.entity.Currency;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CostResult {
    BigDecimal cost;
    Currency currency;
}
