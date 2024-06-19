package com.example.capstone_project.service.impl;

import com.example.capstone_project.entity.Department;
import com.example.capstone_project.repository.CustomDepartmentRepository;
import com.example.capstone_project.repository.DepartmentRepository;

import com.example.capstone_project.service.DepartmentService;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DepartmentServiceImpl implements DepartmentService {
    private final DepartmentRepository departmentRepository;
    private final CustomDepartmentRepository departmentCustomRepository;

    @Override
    public List<Department> getAllDepartments(String query, Pageable pageable) {
        return departmentCustomRepository.getDepartmentWithPagination(query, pageable);
    }

    @Override
    public long countDistinct(String query) {
        String queryPattern = "%" + query + "%";
        return departmentRepository.countDistinct(queryPattern);
    }


}
