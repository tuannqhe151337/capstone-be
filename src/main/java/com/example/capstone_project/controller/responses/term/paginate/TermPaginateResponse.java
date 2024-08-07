package com.example.capstone_project.controller.responses.term.paginate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TermPaginateResponse {
    private Long termId;
    private String name;
    private StatusResponse status;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
}
