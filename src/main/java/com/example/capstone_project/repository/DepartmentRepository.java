package com.example.capstone_project.repository;

import com.example.capstone_project.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface DepartmentRepository extends JpaRepository<Department, Long>, CustomDepartmentRepository {
    @Override
    boolean existsById(Long aLong);

    @Query(value = "SELECT DISTINCT count(department.id) FROM Department department " +
            " WHERE department.name like %:query% AND " +
            " department.isDelete = false ")
    long countDistinct(@Param("query") String query);
}
