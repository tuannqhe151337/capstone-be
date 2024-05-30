package com.example.capstone_project.controller.body.user.create;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Getter
@Builder
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserBody {
    private String username;

    private String email;

    private Long departmentId;

    private String phone;

    private Long roleId;

    private Long positionId;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX", shape = JsonFormat.Shape.STRING)
    private LocalDateTime dob;

    private String address;
}
