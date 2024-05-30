package com.example.capstone_project.controller.responses;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
@Data
@Getter
@Builder
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FinancialPlanFileResponse {
    private Long id;

    private String name;

    private String url;

    private String version;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX", shape = JsonFormat.Shape.STRING)
    private LocalDateTime createdAt;

}
