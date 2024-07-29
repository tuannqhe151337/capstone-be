package com.example.capstone_project.repository;

import com.example.capstone_project.entity.Department;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CustomDepartmentRepository {
    List<Department> getListDepartmentWithPagination(String query, Pageable pageable);

}
