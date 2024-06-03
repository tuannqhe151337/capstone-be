package com.example.capstone_project.controller.responses.term.paginate;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TermPaginateResponse {
    private long termId;
    private String name;
}
