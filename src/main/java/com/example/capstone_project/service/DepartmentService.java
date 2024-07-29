package com.example.capstone_project.service;

import com.example.capstone_project.entity.Department;
import org.springframework.data.domain.Pageable;

import java.util.List;


public interface DepartmentService {

    long countDistinct(String query);

    List<Department> getListDepartmentPaging(String query, Pageable pageable);

    long countDistinctListDepartmentPaging(String query);
}
