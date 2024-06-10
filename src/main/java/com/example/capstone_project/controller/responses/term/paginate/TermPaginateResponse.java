package com.example.capstone_project.controller.responses.term.paginate;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class TermPaginateResponse {
    private long termId;
    private String name;
    private StatusResponse status;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
}
