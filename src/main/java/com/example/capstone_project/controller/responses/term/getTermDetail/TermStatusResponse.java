package com.example.capstone_project.controller.responses.term.getTermDetail;
import com.example.capstone_project.utils.enums.TermCode;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
public class TermStatusResponse {
    private String name;

    @Enumerated(EnumType.STRING)
    private TermCode code;

    private boolean isDelete;
}
