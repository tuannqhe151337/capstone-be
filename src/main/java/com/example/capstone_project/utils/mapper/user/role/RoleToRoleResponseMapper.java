package com.example.capstone_project.utils.mapper.user.role;

import com.example.capstone_project.controller.responses.user.RoleResponse;
import com.example.capstone_project.entity.Role;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RoleToRoleResponseMapper {

    //map role entity to role response
    RoleResponse mapRoleToRoleResponse(Role role);

    List<RoleResponse> mapRolesToRoleResponses(List<Role> roles);


}
