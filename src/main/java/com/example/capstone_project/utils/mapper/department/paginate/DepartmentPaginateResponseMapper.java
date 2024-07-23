package com.example.capstone_project.utils.mapper.department.paginate;

import com.example.capstone_project.controller.responses.department.paginate.DepartmentPaginateResponse;
import com.example.capstone_project.entity.Department;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DepartmentPaginateResponseMapper {
    @Mapping(source = "id",target = "departmentId")
    DepartmentPaginateResponse mapToDepartmentPaginateResponseMapping(Department department);
}
