package com.example.capstone_project.utils.mapper.auth;

import com.example.capstone_project.controller.responses.AuthorityResponse;
import com.example.capstone_project.controller.responses.auth.LoginResponse;
import com.example.capstone_project.entity.Authority;
import com.example.capstone_project.service.result.LoginResult;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface LoginResultResponseMapper {
    @Mapping(source = "tokenPair.accessToken", target = "token")
    @Mapping(source = "tokenPair.refreshToken", target = "refreshToken")
    @Mapping(source = "user.id", target = "data.userId")
    @Mapping(source = "user.username", target = "data.username")
    @Mapping(source = "user.fullName", target = "data.fullName")
    @Mapping(source = "user.role.id", target = "data.role.id")
    @Mapping(source = "user.role.code", target = "data.role.code")
    @Mapping(source = "user.role.name", target = "data.role.name")
    @Mapping(source = "user.department.id", target = "data.department.id")
    @Mapping(source = "user.department.name", target = "data.department.name")
    @Mapping(source = "user.position.id", target = "data.position.id")
    @Mapping(source = "user.position.name", target = "data.position.name")
    @Mapping(source = "user.userSetting.language", target = "data.settings.language")
    @Mapping(source = "user.userSetting.theme", target = "data.settings.theme")
    @Mapping(source = "user.userSetting.darkMode", target = "data.settings.darkMode")
    LoginResponse mapToLoginResponse(LoginResult loginResult);

    AuthorityResponse mapToAuthorityResponse(Authority value);
}
