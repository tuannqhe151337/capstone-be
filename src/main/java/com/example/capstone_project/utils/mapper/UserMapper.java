package com.example.capstone_project.utils.mapper;

import com.example.capstone_project.controller.responses.UserDetailResponse;
import com.example.capstone_project.controller.responses.UserResponse;
import com.example.capstone_project.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserResponse mapToUserResponse(User user);

    User mapToUser(UserResponse userResponse);

    @Mapping(source = "user.department.id", target = "departmentId")
    @Mapping(source = "user.department.name", target = "departmentName")
    @Mapping(source = "user.position.id", target = "positionId")
    @Mapping(source = "user.position.name", target = "positionName  ")
    @Mapping(source = "user.role.id", target = "roleId")
    @Mapping(source = "user.role.code", target = "roleCode")
    @Mapping(source = "user.role.name", target = "roleName")
    UserDetailResponse mapToUserDetail(User user);
}
