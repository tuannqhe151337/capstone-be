package com.example.capstone_project.controller.body.term.update;

import com.example.capstone_project.utils.enums.TermDuration;
import jakarta.persistence.*;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateTermBody {
    @NotNull(message = "ID cannot be empty")
    private Long id;

    @NotEmpty(message = "Name cannot be empty")
    private String name;

    @NotNull(message = "Duration cannot be null")
    @Enumerated(EnumType.STRING)
    private TermDuration duration ;

    @NotNull(message = "Start date cannot be null")
    private LocalDateTime startDate;

    @NotNull(message = "End date cannot be null")
    @Future(message = "End date must be in the future")
    @Column(name = "end_date")
    private LocalDateTime endDate;

    @NotNull(message = "Re-upload start date cannot be null")
    @Future(message = "Re-upload start date must be in the future")
    @Column(name = "start_reupload_date")
    private LocalDateTime reuploadStartDate;


    @NotNull(message = "Re-upload end date cannot be null")
    @Future(message = "Re-upload end date must be in the future")
    @Column(name = "end_reupload_date")
    private LocalDateTime reuploadEndDate;

    //end term date will calculate depending on start date [MONTHLY]
    @Future(message = "Final end term date must be in the future")
    @Column(name = "final_end_term")
    private LocalDateTime finalEndTermDate;


}
