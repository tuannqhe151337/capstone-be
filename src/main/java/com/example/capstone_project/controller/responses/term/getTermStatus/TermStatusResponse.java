package com.example.capstone_project.controller.responses.term.getTermStatus;


import com.example.capstone_project.entity.Term;
import com.example.capstone_project.utils.enums.TermCode;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TermStatusResponse {
    private Long id;
    private String name;
    private TermCode code;

}
