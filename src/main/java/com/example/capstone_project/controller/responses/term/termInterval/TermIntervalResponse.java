package com.example.capstone_project.controller.responses.term.termInterval;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TermIntervalResponse {
    private Long id;
    private int startTermDate;
    private int endTermInterval;
    private int startReuploadInterval;
    private int endReuploadInterval;
}
