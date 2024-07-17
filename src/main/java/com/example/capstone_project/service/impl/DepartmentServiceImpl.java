package com.example.capstone_project.service.impl;

import com.example.capstone_project.entity.Department;
import com.example.capstone_project.repository.CustomDepartmentRepository;
import com.example.capstone_project.repository.DepartmentRepository;

import com.example.capstone_project.service.DepartmentService;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import com.example.capstone_project.repository.DepartmentRepository;
import com.example.capstone_project.repository.redis.UserAuthorityRepository;
import com.example.capstone_project.service.DepartmentService;
import com.example.capstone_project.utils.enums.AuthorityCode;
import com.example.capstone_project.utils.helper.UserHelper;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DepartmentServiceImpl implements DepartmentService {
    private final DepartmentRepository departmentRepository;


    @Override
    public List<Department> getDepartmentWithPagination(String query, Pageable pageable) {
        return departmentRepository.getListDepartmentWithPagination(query, pageable);
    }

    @Override
    public long countDistinct(String query) {
        String queryPattern = "%" + query + "%";
        return departmentRepository.countDistinct(queryPattern);
    }


}
