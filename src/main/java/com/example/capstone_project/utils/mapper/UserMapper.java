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

    @Mapping(source = "user.department.id", target = "departmentResponse.id")
    @Mapping(source = "user.department.name", target = "departmentResponse.name")
    @Mapping(source = "user.position.id", target = "positionResponse.id")
    @Mapping(source = "user.position.name", target = "positionResponse.name")
    @Mapping(source = "user.role.id", target = "roleResponse.id")
    @Mapping(source = "user.role.code", target = "roleResponse.code")
    @Mapping(source = "user.role.name", target = "roleResponse.name")
    UserDetailResponse mapToUserDetail(User user);
}
