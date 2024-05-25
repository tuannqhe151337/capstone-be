package com.example.capstone_project.utils.mapper;

import com.example.capstone_project.controller.responses.RoleResponse;
import com.example.capstone_project.entity.Role;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    RoleResponse mapToRole(Role role);
    Role mapToRole(RoleResponse roleResponse);
}
