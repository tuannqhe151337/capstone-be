package com.example.capstone_project.controller;

import com.example.capstone_project.controller.body.costType.DeleteCostTypeBody;
import com.example.capstone_project.controller.body.costType.NewCostTypeBody;
import com.example.capstone_project.controller.body.costType.UpdateCostTypeBody;
import com.example.capstone_project.controller.body.department.DeleteDepartmentBody;
import com.example.capstone_project.controller.responses.*;
import com.example.capstone_project.controller.responses.department.paginate.DepartmentPaginateResponse;
import com.example.capstone_project.controller.responses.expense.CostTypeResponse;
import com.example.capstone_project.entity.CostType;
import com.example.capstone_project.service.CostTypeService;
import com.example.capstone_project.utils.exception.ResourceNotFoundException;
import com.example.capstone_project.utils.exception.UnauthorizedException;
import com.example.capstone_project.utils.exception.term.InvalidDateException;
import com.example.capstone_project.utils.helper.JwtHelper;
import com.example.capstone_project.utils.helper.PaginationHelper;
import com.example.capstone_project.utils.mapper.expense.CostTypeMapperImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/cost-type")
@RequiredArgsConstructor
public class CostTypeController {
    private final CostTypeService costTypeService;

    @GetMapping("/list-paginate")
    public ResponseEntity<ListPaginationResponse<CostTypeResponse>> getListCostType(
            @RequestParam(required = false) String query,
            @RequestParam(required = false) String page,
            @RequestParam(required = false) String size,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String sortType
    ) {
        try {
            // Handling page and pageSize
            Integer pageInt = PaginationHelper.convertPageToInteger(page);
            Integer sizeInt = PaginationHelper.convertPageSizeToInteger(size);

            // Handling query
            if (query == null) {
                query = "";
            }

            // Handling pagination
            Pageable pageable = PaginationHelper.handlingPagination(pageInt, sizeInt, sortBy, sortType);


            // Get data
            List<CostType> costTypes = costTypeService.getListCostType(query, pageable);

            // Response
            ListPaginationResponse<CostTypeResponse> response = new ListPaginationResponse<>();

            long count = 0;

            if (costTypes != null) {
                // Count total record
                count = costTypeService.countDistinctListCostType(query);

                // Mapping to CostTypeResponse
                response.setData(costTypes.stream().map(x -> {
                    return new CostTypeMapperImpl().mapToCostTypeResponse(x);
                }).toList());
            } else {

                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
            }

            long numPages = PaginationHelper.calculateNumPages(count, sizeInt);

            response.setPagination(Pagination.builder()
                    .totalRecords(count)
                    .page(pageInt)
                    .limitRecordsPerPage(sizeInt)
                    .numPages(numPages)
                    .build());

            return ResponseEntity.ok(response);
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

    }

    @PostMapping("/create")
    public ResponseEntity<ExceptionResponse> createCostType(
            @Valid @RequestBody NewCostTypeBody newCostTypeBody, BindingResult bindingResult
    ) {

        if (bindingResult.hasErrors()) {
            // Xử lý lỗi validation và trả về phản hồi lỗi
            String errorMessage = bindingResult.getAllErrors().stream()
                    .map(ObjectError::getDefaultMessage)
                    .collect(Collectors.joining(", "));
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    ExceptionResponse.builder().field("Validation Error").message(errorMessage).build());
        }

        try {

            // Save plan
            costTypeService.createCostType(newCostTypeBody.getCostTypeName());

            return ResponseEntity.status(HttpStatus.CREATED).body(ExceptionResponse.builder().field("Create").message("Create successful").build());
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ExceptionResponse.builder().field("Error exception").message("Unauthorized").build());
        } catch (DuplicateKeyException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ExceptionResponse.builder().field("Error exception").message("Duplicate name department").build());
        }
    }

    @PutMapping("/update")
    public ResponseEntity<ExceptionResponse> updateCostType(
            @Valid @RequestBody UpdateCostTypeBody updateCostTypeBody, BindingResult bindingResult
    ) {

        if (bindingResult.hasErrors()) {
            // Xử lý lỗi validation và trả về phản hồi lỗi
            String errorMessage = bindingResult.getAllErrors().stream()
                    .map(ObjectError::getDefaultMessage)
                    .collect(Collectors.joining(", "));
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    ExceptionResponse.builder().field("Validation Error").message(errorMessage).build());
        }

        try {

            // Save plan
            costTypeService.updateCostType(CostType.builder().id(updateCostTypeBody.getCostTypeId()).name(updateCostTypeBody.getCostTypeName()).build());

            return ResponseEntity.status(HttpStatus.OK).body(ExceptionResponse.builder().field("Update").message("Update successful").build());
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ExceptionResponse.builder().field("Error exception").message("Unauthorized").build());
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ExceptionResponse.builder().field("Error exception").message("Not found any department have id = " + updateCostTypeBody.getCostTypeId()).build());
        } catch (DuplicateKeyException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ExceptionResponse.builder().field("Error exception").message("Duplicate name cost type").build());
        }
    }

    @DeleteMapping()
    public ResponseEntity<ExceptionResponse> deleteCostType(
            @Valid @RequestBody DeleteCostTypeBody deleteCostTypeBody, BindingResult bindingResult) throws Exception {
        try {

            // Delete plan
            costTypeService.deleteCostType(deleteCostTypeBody.getCostTypeId());

            return ResponseEntity.status(HttpStatus.OK).body(ExceptionResponse.builder().field("Delete").message("Delete successful").build());
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ExceptionResponse.builder().field("Error exception").message("Unauthorized").build());
        } catch (DuplicateKeyException | ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ExceptionResponse.builder().field("Error exception").message("Not found any department have id = " + deleteCostTypeBody.getCostTypeId()).build());
        }
    }

    @GetMapping("/list")
    public ResponseEntity<ListResponse<CostTypeResponse>> getListCostType() {

        // Get data
        List<CostType> costTypes = costTypeService.getListCostType();

        // Response
        ListResponse<CostTypeResponse> responses = new ListResponse<>();

        if (costTypes != null) {

            // Mapping to CostTypeResponse
            responses.setData(costTypes.stream().map(x -> {
                return new CostTypeMapperImpl().mapToCostTypeResponse(x);
            }).toList());
        } else {

            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }

        return ResponseEntity.ok(responses);
    }
}
