package com.example.capstone_project.utils.mapper.user.create;

import com.example.capstone_project.controller.body.user.create.CreateUserBody;
import com.example.capstone_project.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CreateUserBodyMapper {
    @Mapping(source = "userBody.departmentId", target = "department.id")
    @Mapping(source = "userBody.positionId", target = "position.id")
    @Mapping(source = "userBody.roleId", target = "role.id")
    User mapBodytoUser(CreateUserBody userBody);
}
