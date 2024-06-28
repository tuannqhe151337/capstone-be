package com.example.capstone_project.repository;

import com.example.capstone_project.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface DepartmentRepository extends JpaRepository<Department, Long>, CustomDepartmentRepository  {
    @Query(value = "select count(distinct(department.id)) from Department department " +
            "where department.name like %:query% and (department.isDelete = false or department.isDelete is null)")
    long countDistinct(String query);

    boolean existsById(Long id);
}
