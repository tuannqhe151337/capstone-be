package com.example.capstone_project.controller.responses.report;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SupplierResponse {
    private Long supplierId;
    private String name;
}
