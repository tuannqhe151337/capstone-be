package com.example.capstone_project.utils.mapper;

import com.example.capstone_project.controller.responses.TermResponse;
import com.example.capstone_project.entity.Term;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TermMapper {
    TermResponse mapToTermResponse(Term term);
    Term mapToTerm(TermResponse termResponse);
}
