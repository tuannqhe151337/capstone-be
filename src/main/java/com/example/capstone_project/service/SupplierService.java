package com.example.capstone_project.service;

import com.example.capstone_project.entity.Supplier;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface SupplierService {
    List<Supplier> getListSupplierPaging(String query, Pageable pageable);

    long countDistinctListSupplierPaging(String query);

    void createSupplier(String supplierName);

    void deleteSupplier(Long supplierId);

    void updateSupplier(Supplier build);

    List<Supplier> getListSupplier();
}
