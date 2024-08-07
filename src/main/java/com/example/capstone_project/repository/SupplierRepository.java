package com.example.capstone_project.repository;

import com.example.capstone_project.entity.Supplier;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SupplierRepository extends JpaRepository<Supplier,Long>, CustomSupplierRepository {
    @Query(value = " SELECT count(distinct(supplier.id)) from Supplier supplier " +
            " WHERE supplier.name like %:query% AND " +
            " (supplier.isDelete = false OR supplier.isDelete is null) ")
    long countDistinctListSupplierPaging(String query);
}
