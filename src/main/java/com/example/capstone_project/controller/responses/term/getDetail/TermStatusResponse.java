package com.example.capstone_project.controller.responses.term.getDetail;

import com.example.capstone_project.utils.enums.TermCode;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
public class TermStatusResponse {
    private String name;

    @Enumerated(EnumType.STRING)
    private TermCode code;

    private Boolean isDelete;
}
