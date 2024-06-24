package com.example.capstone_project.controller;


import com.example.capstone_project.controller.responses.ListPaginationResponse;
import com.example.capstone_project.controller.responses.ListResponse;
import com.example.capstone_project.controller.responses.Pagination;
import com.example.capstone_project.controller.responses.user.PositionResponse;
import com.example.capstone_project.controller.responses.user.RoleResponse;
import com.example.capstone_project.entity.Position;
import com.example.capstone_project.entity.Role;
import com.example.capstone_project.service.PositionService;
import com.example.capstone_project.utils.helper.PaginationHelper;
import com.example.capstone_project.utils.mapper.user.position.PositionToPositionResponseMapper;
import com.example.capstone_project.utils.mapper.user.position.PositionToPositionResponseMapperImpl;
import lombok.*;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("/api/position")
@RequiredArgsConstructor


public class PositionController {
    private final PositionService positionService;
    @GetMapping("/user-paging-position")
    public ResponseEntity<ListPaginationResponse<PositionResponse>> getListDepartmentPagingUser(
            @RequestParam(required = false) String query,
            @RequestParam(required = false) String page,
            @RequestParam(required = false) String size,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String sortType
    ) {

        int pageInt = PaginationHelper.convertPageToInteger(page) ;
        int sizeInt = PaginationHelper.convertPageSizeToInteger(size);

        // Handling query
        if (query == null) {
            query = "";
        }

        // Handling pagination
        Pageable pageable = PaginationHelper.handlingPagination(pageInt, sizeInt, sortBy, sortType);

        // Get data
        List<Position> positions = positionService.getPositions(query, pageable);


        //map
        List<PositionResponse> departmentResponses =
                new PositionToPositionResponseMapperImpl()
                        .mapPositionToPositionResponses(positions);
        //count , totalrecords
        long totalRecords = positionService.countDistinct(query);
        long numPages = PaginationHelper.calculateNumPages(totalRecords, sizeInt);

        // Response
        ListPaginationResponse<PositionResponse> response = new ListPaginationResponse<>();

        response.setData(departmentResponses);


        response.setPagination(Pagination.builder()
                .totalRecords(totalRecords)
                .page(pageInt)
                .limitRecordsPerPage(sizeInt)
                .numPages(numPages)
                .build());

        return ResponseEntity.ok(response);
    }
}
