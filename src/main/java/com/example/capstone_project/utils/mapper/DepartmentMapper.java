package com.example.capstone_project.utils.mapper;

import com.example.capstone_project.controller.responses.DepartmentResponse;
import com.example.capstone_project.entity.Department;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DepartmentMapper {
   DepartmentResponse mapToDepartmentResponse(Department department);
   Department mapToDepartment(DepartmentResponse departmentResponse);
}
