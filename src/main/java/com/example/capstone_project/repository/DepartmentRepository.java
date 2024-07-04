package com.example.capstone_project.repository;

import com.example.capstone_project.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DepartmentRepository extends JpaRepository<Department, Long>, CustomDepartmentRepository {


    @Query(value = "SELECT count(distinct (department.id)) FROM Department department " +
            " WHERE department.name like %:query% AND " +
            " department.isDelete = false ")
    long countDistinct(@Param("query") String query);

    @Override
    boolean existsById(Long id);

    @Query(value = " SELECT DISTINCT department.id FROM Department department " +
            " JOIN department.plans plan " +
            " JOIN plan.planFiles file " +
            " WHERE file.id = :fileId AND " +
            " file.isDelete = false ")
    long getDepartmentIdByFileId(Long fileId);
}
