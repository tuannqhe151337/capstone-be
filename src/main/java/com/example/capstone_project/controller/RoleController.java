package com.example.capstone_project.controller;


import com.example.capstone_project.controller.responses.ListResponse;
import com.example.capstone_project.controller.responses.user.PositionResponse;
import com.example.capstone_project.controller.responses.user.RoleResponse;
import com.example.capstone_project.service.RoleService;
import com.example.capstone_project.utils.mapper.user.role.RoleToRoleResponseMapper;
import com.example.capstone_project.utils.mapper.user.role.RoleToRoleResponseMapperImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/role")
@RequiredArgsConstructor
public class RoleController {
    private final RoleService roleService;

    @GetMapping("/user-paging-role")
    public ResponseEntity<ListResponse<RoleResponse>> getListRolePagingUser() {
        ListResponse<RoleResponse> roleList = new ListResponse<>();
        List<RoleResponse> roles =
                new RoleToRoleResponseMapperImpl()
                        .mapRolesToRoleResponses(roleService.getRoles());
        roleList.setData(roles);
        return ResponseEntity.ok(roleList);
    }
}
