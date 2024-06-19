package com.example.capstone_project.service;
import com.example.capstone_project.entity.Department;
import com.example.capstone_project.repository.DepartmentRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;


public interface DepartmentService {

   List<Department> getAllDepartments
           (String query, Pageable pageable);
   long countDistinct(String query);
}
