package com.example.capstone_project.utils.mapper.user.edit;


import com.example.capstone_project.controller.body.user.update.UpdateUserBody;
import com.example.capstone_project.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UpdateUserBodyToUserEntityMapper {
    @Mapping(source = "department", target = "department.id")
    @Mapping(source = "role", target = "role.id")
    @Mapping(source = "position", target = "position.id")
    User updateUserFromDto(UpdateUserBody dto, @MappingTarget User entity);
}
