package com.example.capstone_project.repository;

import com.example.capstone_project.entity.Supplier;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CustomSupplierRepository {
    List<Supplier> getSupplierWithPagination(String query, Pageable pageable);
}
