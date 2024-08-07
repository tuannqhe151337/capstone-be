package com.example.capstone_project.controller.body.term.create;

import com.example.capstone_project.utils.enums.TermDuration;
import jakarta.persistence.*;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;


@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CreateTermBody {
    @NotEmpty(message = "Name can not be empty")
    private String name;

    @NotNull(message = "Duration cannot be null")
    @Enumerated(EnumType.STRING)
    private TermDuration duration;

    @NotNull(message = "Start date cannot be null")
    private LocalDateTime startDate;

    @NotNull(message = "End date cannot be null")
    @Future(message = "End date must be in the future")
    private LocalDateTime endDate;

    @NotNull(message = "Re-upload start date cannot be null")
    @Future(message = "Re-upload start date must be in the future")
    private LocalDateTime reuploadStartDate;


    @NotNull(message = "Re-upload end date cannot be null")
    @Future(message = "Re-upload end date must be in the future")
    private LocalDateTime reuploadEndDate;

    //end term date will calculate depending on start date [MONTHLY]
    @Future(message = "Final end term date must be in the future")
    private LocalDateTime finalEndTermDate;

    @NotNull(message = "allowReupload cannot be null")
    private boolean allowReupload;
}
