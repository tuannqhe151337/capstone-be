package com.example.capstone_project.controller.responses.user.detail;

import com.example.capstone_project.controller.responses.user.DepartmentResponse;
import com.example.capstone_project.controller.responses.user.PositionResponse;
import com.example.capstone_project.controller.responses.user.RoleResponse;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Getter
@Builder
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDetailResponse {

    private Long id;

    private String username;

    private String fullName;

    private String email;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS", shape = JsonFormat.Shape.STRING)
    private LocalDateTime dob;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS", shape = JsonFormat.Shape.STRING)
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS", shape = JsonFormat.Shape.STRING)
    private LocalDateTime updatedAt;

    private String note;

    private String phoneNumber;

    private String address;

    private DepartmentResponse department;

    private PositionResponse position;

    private RoleResponse role;



}





