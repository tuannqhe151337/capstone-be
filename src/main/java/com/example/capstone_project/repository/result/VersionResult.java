package com.example.capstone_project.repository.result;

import lombok.*;

import java.time.LocalDateTime;
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class VersionResult {
    Long fileId;
    Long version;
    LocalDateTime createdAt;
    Long userId;
    String username;
}