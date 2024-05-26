package com.example.capstone_project.utils.mapper;

import com.example.capstone_project.controller.responses.AuthorityResponse;
import com.example.capstone_project.controller.responses.auth.UserDataResponse;
import com.example.capstone_project.entity.Authority;
import com.example.capstone_project.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserEntityDetailResponseMapper {
    @Mapping(source = "id", target = "userId")
    @Mapping(source = "username", target = "username")
    @Mapping(source = "fullName", target = "fullName")
    @Mapping(source = "role.id", target = "role.id")
    @Mapping(source = "role.code", target = "role.code")
    @Mapping(source = "role.name", target = "role.name")
    @Mapping(source = "department.id", target = "department.id")
    @Mapping(source = "department.name", target = "department.name")
    @Mapping(source = "position.id", target = "position.id")
    @Mapping(source = "position.name", target = "position.name")
    @Mapping(source = "userSetting.language", target = "settings.language")
    @Mapping(source = "userSetting.theme", target = "settings.theme")
    @Mapping(source = "userSetting.darkMode", target = "settings.darkMode")
    UserDataResponse mapToUserDataResponse(User user);

    AuthorityResponse mapToAuthorityResponse(Authority value);
}
