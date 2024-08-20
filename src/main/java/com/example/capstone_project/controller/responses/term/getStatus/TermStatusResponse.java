package com.example.capstone_project.controller.responses.term.getStatus;

import com.example.capstone_project.utils.enums.TermStatusCode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TermStatusResponse {
    private Long id;
    private String name;
    private TermStatusCode code;

}