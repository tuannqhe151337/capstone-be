package com.example.capstone_project.controller.responses.auth;

import lombok.Data;

@Data
public class UserSettingResponse {
   private String language;
   private String theme;
   private boolean darkMode;
}
