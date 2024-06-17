package com.example.capstone_project.controller;


import com.example.capstone_project.controller.responses.ListResponse;
import com.example.capstone_project.controller.responses.user.PositionResponse;
import com.example.capstone_project.controller.responses.user.RoleResponse;
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
    @GetMapping("/user-paging-role")
    public ResponseEntity<ListResponse<RoleResponse>> getListRolePagingUser() {
        ListResponse<RoleResponse> roleList = new ListResponse<>();
        roleList.setData(List.of(
                RoleResponse.builder()
                        .id(1L)
                        .name("Admin")
                        .code("admin")
                        .build(),
                RoleResponse.builder()
                        .id(2L)
                        .name("Accountant")
                        .code("accountant")
                        .build(),
                RoleResponse.builder()
                        .id(3L)
                        .name("Financial Staff")
                        .code("financial-staff")
                        .build() ));

        return ResponseEntity.ok(roleList);
    }
}
