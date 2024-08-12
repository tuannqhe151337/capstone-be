package com.example.capstone_project.utils.mapper.term.termInterval;

import com.example.capstone_project.controller.responses.term.termInterval.TermIntervalResponse;
import com.example.capstone_project.entity.TermInterval;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TermIntervalMapper {
    TermIntervalResponse mapToTermIntervalResponse(TermInterval termInterval);
}
