package com.example.capstone_project.utils.mapper;

import com.example.capstone_project.controller.responses.user.UserResponse;
import com.example.capstone_project.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(source = "id", target = "userId")
    @Mapping(source = "username", target = "username")
    @Mapping(source = "email", target = "email")
    @Mapping(source = "role.id", target = "role.id")
    @Mapping(source = "role.code", target = "role.code")
    @Mapping(source = "role.name", target = "role.name")
    @Mapping(source = "department.id", target = "department.id")
    @Mapping(source = "department.name", target = "department.name")
    @Mapping(source = "position.id", target = "position.id")
    @Mapping(source = "position.name", target = "position.name")
    @Mapping(source = "delete", target = "deactivate") // Don't know why must change isDelete to delete in entity but it works
    UserResponse mapToUserResponse(User user);
    User mapToUser(UserResponse userResponse);
}
