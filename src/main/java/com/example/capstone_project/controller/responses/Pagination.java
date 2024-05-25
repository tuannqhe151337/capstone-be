package com.example.capstone_project.controller.responses;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class Pagination {
    private long page;

    private long count;

    private long displayRecord;

    private long numPages;
}
