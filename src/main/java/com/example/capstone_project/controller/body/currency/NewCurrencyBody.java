package com.example.capstone_project.controller.body.currency;

import jakarta.validation.constraints.NotEmpty;
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
    @Pattern(regexp = "^[a-zA-ZÀ-ỹ0-9]{3}(?: [a-zA-ZÀ-ỹ0-9]+)*$", message = "Project name must only contain 3 letters")
    private String currencyName;
}
