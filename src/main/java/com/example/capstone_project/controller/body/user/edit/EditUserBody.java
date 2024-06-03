package com.example.capstone_project.controller.body.user.edit;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Getter
@Builder
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EditUserBody {

    private String id;

    private String username;

    private String fullName;

    private String email;

    private Long departmentId;

    private String phoneNumber;

    private Long roleId;

    private Long positionId;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS", shape = JsonFormat.Shape.STRING)
    private LocalDateTime dob;

    private String address;


}
