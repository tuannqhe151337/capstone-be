package com.example.capstone_project.utils.mapper.user.edit;


import com.example.capstone_project.controller.body.user.update.UpdateUserBody;
import com.example.capstone_project.controller.responses.user.create.UserDetailResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UpdateUserToUserDetailResponseMapper {
    @Mapping(source = "updateUserBody.departmentId", target = "department.id")
    @Mapping(source = "updateUserBody.positionId", target = "position.id")
    @Mapping(source = "updateUserBody.roleId", target = "role.id")
    UserDetailResponse mapUpdateUserToUserDetailResponse(UpdateUserBody updateUserBody);
}
