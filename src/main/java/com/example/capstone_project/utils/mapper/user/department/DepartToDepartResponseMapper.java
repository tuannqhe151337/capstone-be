package com.example.capstone_project.utils.mapper.user.department;
import com.example.capstone_project.controller.responses.user.DepartmentResponse;
import com.example.capstone_project.entity.Department;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DepartToDepartResponseMapper {
    //map department entity to department response
      DepartmentResponse mapDepartmentToDepartmentResponse(Department department);

      List<DepartmentResponse> mapDepartmentsToDepartmentResponses(List<Department> departments);
}
