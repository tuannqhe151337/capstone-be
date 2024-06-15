package com.example.capstone_project.controller;


import com.example.capstone_project.controller.responses.ListPaginationResponse;
import com.example.capstone_project.controller.responses.ListResponse;
import com.example.capstone_project.controller.responses.user.PositionResponse;
import com.example.capstone_project.service.PositionService;
import com.example.capstone_project.utils.mapper.user.position.PositionToPositionResponseMapper;
import com.example.capstone_project.utils.mapper.user.position.PositionToPositionResponseMapperImpl;
import lombok.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("/api/position")
@RequiredArgsConstructor
public class PositionController {
    private final PositionService positionService;
    @GetMapping("/user-paging-position")
    public ResponseEntity<ListResponse<PositionResponse>> getListPositionPagingUser(){
        ListResponse<PositionResponse> positionlist = new ListResponse<>();
        List<PositionResponse> positions =
                new PositionToPositionResponseMapperImpl()
                        .mapPositionToPositionResponses(positionService.getPositions());
        positionlist.setData(positions);
        return ResponseEntity.ok(positionlist);
    }
}
