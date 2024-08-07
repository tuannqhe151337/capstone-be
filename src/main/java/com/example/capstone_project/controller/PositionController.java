package com.example.capstone_project.controller;


import com.example.capstone_project.controller.body.department.DeleteDepartmentBody;
import com.example.capstone_project.controller.body.department.NewDepartmentBody;
import com.example.capstone_project.controller.body.department.UpdateDepartmentBody;
import com.example.capstone_project.controller.body.position.DeletePositionBody;
import com.example.capstone_project.controller.body.position.NewPositionBody;
import com.example.capstone_project.controller.body.position.UpdatePositionBody;
import com.example.capstone_project.controller.responses.ExceptionResponse;
import com.example.capstone_project.controller.responses.ListPaginationResponse;
import com.example.capstone_project.controller.responses.ListResponse;
import com.example.capstone_project.controller.responses.Pagination;
import com.example.capstone_project.controller.responses.user.PositionResponse;
import com.example.capstone_project.controller.responses.user.RoleResponse;
import com.example.capstone_project.entity.Department;
import com.example.capstone_project.entity.Position;
import com.example.capstone_project.entity.Role;
import com.example.capstone_project.service.PositionService;
import com.example.capstone_project.utils.exception.ResourceNotFoundException;
import com.example.capstone_project.utils.exception.UnauthorizedException;
import com.example.capstone_project.utils.exception.term.InvalidDateException;
import com.example.capstone_project.utils.helper.PaginationHelper;
import com.example.capstone_project.utils.mapper.user.position.PositionToPositionResponseMapper;
import com.example.capstone_project.utils.mapper.user.position.PositionToPositionResponseMapperImpl;
import jakarta.validation.Valid;
import lombok.*;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


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

        int pageInt = PaginationHelper.convertPageToInteger(page);
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


    @PostMapping("/create")
    public ResponseEntity<ExceptionResponse> createNewPosition(
            @Valid @RequestBody NewPositionBody newPositionBody, BindingResult bindingResult) throws Exception {
        if (bindingResult.hasErrors()) {
            // Xử lý lỗi validation và trả về phản hồi lỗi
            String errorMessage = bindingResult.getAllErrors().stream()
                    .map(ObjectError::getDefaultMessage)
                    .collect(Collectors.joining(", "));
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    ExceptionResponse.builder().field("Validation Error").message(errorMessage).build());
        }

        try {

            // Save position
            positionService.createPosition(newPositionBody.getPositionName());

            return ResponseEntity.status(HttpStatus.CREATED).body(ExceptionResponse.builder().field("Create").message("Create successful").build());
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ExceptionResponse.builder().field("Error exception").message("Unauthorized").build());
        } catch (DuplicateKeyException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ExceptionResponse.builder().field("Error exception").message("Duplicate name position").build());
        }
    }

    @DeleteMapping()
    public ResponseEntity<ExceptionResponse> deletePosition(
            @Valid @RequestBody DeletePositionBody deletePositionBody, BindingResult bindingResult) throws Exception {
        try {

            // Delete position
            positionService.deletePosition(deletePositionBody.getPositionId());

            return ResponseEntity.status(HttpStatus.OK).body(ExceptionResponse.builder().field("Delete").message("Delete successful").build());
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ExceptionResponse.builder().field("Error exception").message("Unauthorized").build());
        } catch (DuplicateKeyException | ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ExceptionResponse.builder().field("Error exception").message("Not found any position have id = " + deletePositionBody.getPositionId()).build());
        }
    }

    @PutMapping("/update")
    public ResponseEntity<ExceptionResponse> deleteDepartment(
            @Valid @RequestBody UpdatePositionBody updatePositionBody, BindingResult bindingResult) throws Exception {

        if (bindingResult.hasErrors()) {
            // Xử lý lỗi validation và trả về phản hồi lỗi
            String errorMessage = bindingResult.getAllErrors().stream()
                    .map(ObjectError::getDefaultMessage)
                    .collect(Collectors.joining(", "));
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    ExceptionResponse.builder().field("Validation Error").message(errorMessage).build());
        }

        try {

            // Update position
            positionService.updatePosition(Position.builder().id(updatePositionBody.getPositionId()).name(updatePositionBody.getPositionName()).build());

            return ResponseEntity.status(HttpStatus.OK).body(ExceptionResponse.builder().field("Update").message("Update successful").build());
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ExceptionResponse.builder().field("Error exception").message("Unauthorized").build());
        } catch (DuplicateKeyException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ExceptionResponse.builder().field("Error exception").message("Duplicate name position").build());
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ExceptionResponse.builder().field("Error exception").message("Not found any position have id = " + updatePositionBody.getPositionId()).build());
        }
    }
}
