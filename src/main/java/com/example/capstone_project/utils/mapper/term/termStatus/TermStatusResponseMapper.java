package com.example.capstone_project.utils.mapper.term.termStatus;

import com.example.capstone_project.controller.responses.term.getTermDetail.TermStatusResponse;
import com.example.capstone_project.entity.TermStatus;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TermStatusResponseMapper {

    TermStatusResponse termStatusToTermStatusResponse(TermStatus termStatus) ;
}
