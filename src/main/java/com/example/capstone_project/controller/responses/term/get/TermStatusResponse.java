package com.example.capstone_project.controller.responses.term.get;

import com.example.capstone_project.entity.Term;
import com.example.capstone_project.utils.enums.TermStatusName;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
public class TermStatusResponse {

    @Column(name = "name")
    @Enumerated(EnumType.STRING)
    private TermStatusName name;

    @Column(name = "icon_code")
    private String iconCode;

    @Column(name = "is_delete", columnDefinition = "bit default 0")
    private Boolean isDelete;
}
