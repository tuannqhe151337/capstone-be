package com.example.capstone_project.utils.mapper.user.detail;
import com.example.capstone_project.controller.responses.user.detail.UserDetailResponse;
import com.example.capstone_project.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DetailUserResponseMapper {
    @Mapping(source = "user.department.id", target = "department.id")
    @Mapping(source = "user.department.name", target = "department.name")
    @Mapping(source = "user.position.id", target = "position.id")
    @Mapping(source = "user.position.name", target = "position.name")
    @Mapping(source = "user.role.id", target = "role.id")
    @Mapping(source = "user.role.code", target = "role.code")
    @Mapping(source = "user.role.name", target = "role.name")
    UserDetailResponse mapToUserDetail(User user);
}
