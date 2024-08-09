package com.example.capstone_project.controller.body.supplier;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DeleteSupplierBody {
    @NotNull(message = "Supplier id can't be empty")
    private Long supplierId;
}
