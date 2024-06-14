package com.example.capstone_project.controller.responses;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Pagination {
    private long page;

    private long totalRecords;

    private long limitRecordsPerPage;

    private long numPages;
}
