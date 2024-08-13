package com.example.capstone_project.controller.body.plan.checkUserExist;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CheckUserExistBody {
    private List<String> usernameList;
}
