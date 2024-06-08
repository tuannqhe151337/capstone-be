package com.example.capstone_project.controller.responses.term.get;
import com.example.capstone_project.entity.PlanStatus;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor


public class TermPlanDetalResponse {
    @NotNull(message = "Id cannot be null")
    private Long id;

    @NotEmpty(message = "Name cannot be empty ")
    private String name;

    @NotEmpty(message = "Status cannot be empty ")
    private PlanStatus status;

    @NotNull(message = "isDelete cannot be null")
    private Boolean isDelete;


}
