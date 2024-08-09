package com.example.capstone_project.controller;

import com.example.capstone_project.controller.body.supplier.DeleteSupplierBody;
import com.example.capstone_project.controller.body.supplier.NewSupplierBody;
import com.example.capstone_project.controller.body.supplier.UpdateSupplierBody;
import com.example.capstone_project.controller.responses.ExceptionResponse;
import com.example.capstone_project.controller.responses.ListPaginationResponse;
import com.example.capstone_project.controller.responses.ListResponse;
import com.example.capstone_project.controller.responses.Pagination;
import com.example.capstone_project.controller.responses.supplier.SupplierPaginateResponse;
import com.example.capstone_project.entity.Supplier;
import com.example.capstone_project.service.SupplierService;
import com.example.capstone_project.utils.exception.ResourceNotFoundException;
import com.example.capstone_project.utils.exception.UnauthorizedException;
import com.example.capstone_project.utils.helper.PaginationHelper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/supplier")
@RequiredArgsConstructor
public class SupplierController {
    private final SupplierService supplierService;

    @GetMapping("/list-paginate")
    public ResponseEntity<ListPaginationResponse<SupplierPaginateResponse>> getListSupplierPaging(
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
            List<Supplier> suppliers = supplierService.getListSupplierPaging(query, pageable);

            // Response
            ListPaginationResponse<SupplierPaginateResponse> response = new ListPaginationResponse<>();

            long count = 0;

            if (suppliers != null) {

                // Count total record
                count = supplierService.countDistinctListSupplierPaging(query);

                suppliers.forEach(supplier -> response.getData().add(SupplierPaginateResponse.builder()
                        .supplierId(supplier.getId())
                        .name(supplier.getName())
                        .createdAt(supplier.getCreatedAt())
                        .updatedAt(supplier.getUpdatedAt())
                        .build()));

            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
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
    public ResponseEntity<ExceptionResponse> createSupplier(
            @Valid @RequestBody NewSupplierBody newSupplierBody, BindingResult bindingResult) throws Exception {
        if (bindingResult.hasErrors()) {
            // Xử lý lỗi validation và trả về phản hồi lỗi
            String errorMessage = bindingResult.getAllErrors().stream()
                    .map(ObjectError::getDefaultMessage)
                    .collect(Collectors.joining(", "));
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    ExceptionResponse.builder().field("Validation Error").message(errorMessage).build());
        }

        try {

            supplierService.createSupplier(newSupplierBody.getSupplierName());

            return ResponseEntity.status(HttpStatus.CREATED).body(ExceptionResponse.builder().field("Create").message("Create successful").build());
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ExceptionResponse.builder().field("Error exception").message("Unauthorized").build());
        } catch (DuplicateKeyException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ExceptionResponse.builder().field("Error exception").message("Duplicate name supplier").build());
        }
    }

    @DeleteMapping()
    public ResponseEntity<ExceptionResponse> deleteSupplier(
            @Valid @RequestBody DeleteSupplierBody deleteSupplierBody, BindingResult bindingResult) throws Exception {
        try {

            // Delete plan
            supplierService.deleteSupplier(deleteSupplierBody.getSupplierId());

            return ResponseEntity.status(HttpStatus.OK).body(ExceptionResponse.builder().field("Delete").message("Delete successful").build());
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ExceptionResponse.builder().field("Error exception").message("Unauthorized").build());
        } catch (DuplicateKeyException | ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ExceptionResponse.builder().field("Error exception").message("Not found any supplier have id = " + deleteSupplierBody.getSupplierId()).build());
        }
    }

    @PutMapping("/update")
    public ResponseEntity<ExceptionResponse> updateSupplier(
            @Valid @RequestBody UpdateSupplierBody updateSupplierBody, BindingResult bindingResult) throws Exception {

        if (bindingResult.hasErrors()) {
            // Xử lý lỗi validation và trả về phản hồi lỗi
            String errorMessage = bindingResult.getAllErrors().stream()
                    .map(ObjectError::getDefaultMessage)
                    .collect(Collectors.joining(", "));
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    ExceptionResponse.builder().field("Validation Error").message(errorMessage).build());
        }

        try {

            supplierService.updateSupplier(Supplier.builder().id(updateSupplierBody.getSupplierId()).name(updateSupplierBody.getSupplierName()).build());

            return ResponseEntity.status(HttpStatus.OK).body(ExceptionResponse.builder().field("Update").message("Update successful").build());
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ExceptionResponse.builder().field("Error exception").message("Unauthorized").build());
        } catch (DuplicateKeyException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ExceptionResponse.builder().field("Error exception").message("Duplicate name supplier").build());
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ExceptionResponse.builder().field("Error exception").message("Not found any supplier have id = " + updateSupplierBody.getSupplierId()).build());
        }
    }

    @GetMapping("/list")
    public ResponseEntity<ListResponse<SupplierPaginateResponse>> getListSupplier() {

        // Get data
        List<Supplier> suppliers = supplierService.getListSupplier();

        // Response
        ListResponse<SupplierPaginateResponse> responses = new ListResponse<>();

        if (suppliers != null) {
            suppliers.forEach(supplier -> {
                responses.getData().add(SupplierPaginateResponse.builder()
                        .supplierId(supplier.getId())
                        .name(supplier.getName())
                        .createdAt(supplier.getCreatedAt())
                        .updatedAt(supplier.getUpdatedAt())
                        .build());
            });
        } else {

            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }

        return ResponseEntity.ok(responses);
    }
}
