package com.example.capstone_project.controller.body.user.updateUserSetting;


import com.example.capstone_project.entity.User;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UpdateUserSettingBody {
   @NotEmpty(message = "Language cannot be empty")
    private String language;
   @NotEmpty(message = "Theme cannot be empty")
    private String theme;
   @NotNull
    private boolean darkMode;

}
