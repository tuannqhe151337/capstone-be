package com.example.capstone_project.controller.responses.expense;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PicResponse {
    private Long picId;
    private String name;
}
