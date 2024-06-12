package com.example.capstone_project.controller;


import com.example.capstone_project.controller.responses.ListResponse;
import com.example.capstone_project.controller.responses.Pagination;
import com.example.capstone_project.controller.responses.department.paginate.DepartmentPaginateResponse;
import com.example.capstone_project.controller.responses.user.PositionResponse;
import com.example.capstone_project.repository.PositionRepository;
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
    @GetMapping("/user-paging-position")
    public ResponseEntity<List<PositionResponse>> getListPositionPagingUser(){
        ListResponse<PositionResponse> listResponse = new ListResponse<>();
        List<PositionResponse> positionlist = new ArrayList<>();
        positionlist = List.of(
                PositionResponse.builder()
                        .id(1L)
                        .name("Tech lead")
                        .build(),
                PositionResponse.builder()
                        .id(2L)
                        .name("Staff")
                        .build(),
                PositionResponse.builder()
                        .id(3L)
                        .name("development")
                        .build()
        );

        return ResponseEntity.ok(positionlist);
    }
}
