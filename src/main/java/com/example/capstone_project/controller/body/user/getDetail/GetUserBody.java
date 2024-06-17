package com.example.capstone_project.controller.body.user.getDetail;


import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetUserBody {
    @NotNull(message = "Id cannot be null")
    private Long id;
}
