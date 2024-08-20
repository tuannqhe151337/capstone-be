package com.example.capstone_project.utils.mapper.termStatus;


import com.example.capstone_project.controller.responses.term.getStatus.TermStatusResponse;
import com.example.capstone_project.entity.TermStatus;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TermStatusToTermStatusResponseMapper {

    TermStatusResponse mapTermStatusToTermStatusResponse(TermStatus termStatus);

    List<TermStatusResponse> mmapTermStatusToTermStatusResponseList(List<TermStatus> termStatuses);
}
