package com.example.capstone_project.utils.mapper;

import com.example.capstone_project.controller.responses.UserResponse;
import com.example.capstone_project.entity.User;
import org.mapstruct.Mapper;
@Mapper(componentModel = "spring")
public interface UserMapper {
    UserResponse mapToUserResponse(User user);
    User mapToUser(UserResponse userResponse);
}
