package com.example.capstone_project.controller.responses.user.diagram;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserOverTimeDiagramResponse {
    private String month;
    private int numberUserCreated;
    private int numberUserDeleted;
}
