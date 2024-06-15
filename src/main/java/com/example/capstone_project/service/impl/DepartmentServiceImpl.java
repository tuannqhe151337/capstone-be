package com.example.capstone_project.service.impl;

import com.example.capstone_project.entity.Department;
import com.example.capstone_project.repository.DepartmentRepository;
import com.example.capstone_project.repository.redis.UserAuthorityRepository;
import com.example.capstone_project.service.DepartmentService;
import com.example.capstone_project.utils.enums.AuthorityCode;
import com.example.capstone_project.utils.helper.UserHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DepartmentServiceImpl implements DepartmentService {
    private final DepartmentRepository departmentRepository;
    private final UserAuthorityRepository userAuthorityRepository;

    @Override
    public List<Department> getAllDepartments() {
        List<Department> departments = departmentRepository.findAllByOrderByNameAsc();
        return departments;
    }
}
