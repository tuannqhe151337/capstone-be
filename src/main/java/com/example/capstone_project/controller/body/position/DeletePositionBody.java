package com.example.capstone_project.controller.body.position;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DeletePositionBody {
    @NotNull(message = "Position id can't be empty")
    private Long positionId;
}
