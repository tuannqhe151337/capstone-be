package com.example.capstone_project.service;

import com.example.capstone_project.entity.Department;
import com.example.capstone_project.entity.Term;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface DepartmentService {
    List<Department> getListDepartmentPaging(String query, Pageable pageable);

    long countDistinctListDepartmentPaging(String query);
}
