package com.example.capstone_project.utils.mapper.user.edit;



import com.example.capstone_project.controller.body.user.edit.EditUserBody;
import com.example.capstone_project.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface EditUserResponseEntityMapper {
    @Mapping(source = "editUserBody.departmentId", target = "department.id")
    @Mapping(source = "editUserBody.positionId", target = "position.id")
    @Mapping(source = "editUserBody.roleId", target = "role.id")
    User mapEditResponsetoUser(EditUserBody editUserBody);
}
