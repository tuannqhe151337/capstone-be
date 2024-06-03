package com.example.capstone_project.controller.body.user.delete;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DeleteUserBody {
    @NotEmpty(message = "Plan Id need to delete can't be empty")
    private long id;
}
