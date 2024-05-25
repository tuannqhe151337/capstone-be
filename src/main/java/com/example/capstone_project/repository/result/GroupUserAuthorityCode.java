package com.example.capstone_project.repository.result;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@Builder
@ToString
public class GroupUserAuthorityCode {
    private Long userId;
    private Long groupId;
    private String authorityCode;
}
