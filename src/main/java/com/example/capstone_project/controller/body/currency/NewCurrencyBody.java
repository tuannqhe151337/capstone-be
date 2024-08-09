package com.example.capstone_project.controller.body.currency;

import com.example.capstone_project.utils.enums.Affix;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NewCurrencyBody {
    @NotEmpty(message = "New currency name can't be empty")
    @Pattern(regexp = "^[a-zA-ZÀ-ỹ]{3}(?: [a-zA-ZÀ-ỹ]+)*$", message = "Currency name must only contain 3 letters")
    private String currencyName;

    @NotEmpty(message = "Currency symbol can't be empty")
    private String currencySymbol;

    @NotNull(message = "Currency affix can't be null")
    private Affix currencyAffix;
}
