package com.example.capstone_project.controller.responses.supplier;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SupplierPaginateResponse {
    private Long supplierId;
    private String name;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
