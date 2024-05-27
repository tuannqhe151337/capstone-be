package com.example.capstone_project.utils.mapper;

import com.example.capstone_project.controller.responses.user.PositionResponse;
import com.example.capstone_project.entity.Position;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PositionMapper {
    PositionResponse mapToPositionResponse(Position position);
    Position mapToPosition(PositionResponse positionResponse);
}
