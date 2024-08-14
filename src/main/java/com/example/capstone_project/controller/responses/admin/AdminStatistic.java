package com.example.capstone_project.controller.responses.admin;

import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AdminStatistic {
    private long totalDepartment;
    private long totalEmployee;
    private long totalPosition;
}
