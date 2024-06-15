package com.example.capstone_project.service;
import com.example.capstone_project.entity.Department;
import com.example.capstone_project.repository.DepartmentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface DepartmentService {

   List<Department> getAllDepartments();
}
