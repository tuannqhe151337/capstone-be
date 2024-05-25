package com.example.capstone_project.utils.mapper;

import com.example.capstone_project.controller.responses.AuthorityResponse;
import com.example.capstone_project.entity.Authority;
import org.mapstruct.Mapper;
@Mapper(componentModel = "spring")
public interface AuthorityMapper {
    AuthorityResponse mapToAuthorityResponse(Authority groupAuthority);
    Authority mapToAuthority(AuthorityResponse authorityResponse);
}
