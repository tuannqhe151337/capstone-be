package com.example.capstone_project.utils.mapper.user.list;

import com.example.capstone_project.controller.responses.user.list.UserResponse;
import com.example.capstone_project.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserEntityToUserResponseMapper {

    UserResponse mapToUserResponse(User user);

    List<UserResponse> mapToUserResponseList(List<User> users);
}
