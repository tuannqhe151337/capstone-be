package com.example.capstone_project.repository.result;

import lombok.*;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UpdateUserDataOption {
    private boolean ignoreFullName;
    private boolean ignoreUsername;
    private boolean ignoreEmail;
    private boolean ignoreDepartment;
    private boolean ignorePosition;
    private boolean ignoreRole;
}
