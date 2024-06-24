package com.example.capstone_project.utils.mapper.user.position;

import com.example.capstone_project.controller.responses.user.PositionResponse;
import com.example.capstone_project.entity.Position;
import org.mapstruct.Mapper;

import java.util.List;


@Mapper(componentModel = "spring")
public interface PositionToPositionResponseMapper {
    //map position entity to positon response
    PositionResponse mapPositionToPositionResponse(Position position);
    List<PositionResponse> mapPositionToPositionResponses(List<Position> positions);

}
