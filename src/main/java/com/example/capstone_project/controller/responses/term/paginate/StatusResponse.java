package com.example.capstone_project.controller.responses.term.paginate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StatusResponse {
    private long statusId;
    private String code;
    private String name;
}
