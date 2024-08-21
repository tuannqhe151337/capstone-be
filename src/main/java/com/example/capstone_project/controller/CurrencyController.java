package com.example.capstone_project.controller;

import com.example.capstone_project.controller.body.currency.DeleteCurrencyBody;
import com.example.capstone_project.controller.body.currency.NewCurrencyBody;
import com.example.capstone_project.controller.body.currency.UpdateCurrencyBody;
import com.example.capstone_project.controller.responses.ExceptionResponse;
import com.example.capstone_project.controller.responses.ListPaginationResponse;
import com.example.capstone_project.controller.responses.ListResponse;
import com.example.capstone_project.controller.responses.Pagination;
import com.example.capstone_project.controller.responses.currency.CurrencyPaginateResponse;
import com.example.capstone_project.entity.Currency;
import com.example.capstone_project.service.CurrencyService;
import com.example.capstone_project.utils.exception.InvalidInputException;
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
@RequestMapping("/api/currency")
@RequiredArgsConstructor
public class CurrencyController {
    private final CurrencyService currencyService;

    @GetMapping("/list-paginate")
    public ResponseEntity<ListPaginationResponse<CurrencyPaginateResponse>> getListCurrencyPaging(
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
            List<Currency> currencies = currencyService.getListCurrencyPaging(query, pageable);

            // Response
            ListPaginationResponse<CurrencyPaginateResponse> response = new ListPaginationResponse<>();

            long count = 0;

            if (currencies != null) {

                // Count total record
                count = currencyService.countDistinctListCurrencyPaging(query);

                currencies.forEach(currency -> response.getData().add(CurrencyPaginateResponse.builder()
                        .currencyId(currency.getId())
                        .name(currency.getName())
                        .symbol(currency.getSymbol())
                        .affix(currency.getAffix())
                        .isDefault(currency.isDefault())
                        .createdAt(currency.getCreatedAt())
                        .updatedAt(currency.getUpdatedAt())
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
    public ResponseEntity<ExceptionResponse> createCurrency(
            @Valid @RequestBody NewCurrencyBody newCurrencyBody, BindingResult bindingResult) throws Exception {
        if (bindingResult.hasErrors()) {
            // Xử lý lỗi validation và trả về phản hồi lỗi
            String errorMessage = bindingResult.getAllErrors().stream()
                    .map(ObjectError::getDefaultMessage)
                    .collect(Collectors.joining(", "));
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    ExceptionResponse.builder().field("Validation Error").message(errorMessage).build());
        }

        try {

            currencyService.createCurrency(Currency.builder()
                    .name(newCurrencyBody.getCurrencyName())
                    .symbol(newCurrencyBody.getCurrencySymbol())
                    .affix(newCurrencyBody.getCurrencyAffix())
                    .build());

            return ResponseEntity.status(HttpStatus.CREATED).body(ExceptionResponse.builder().field("Create").message("Create successful").build());
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ExceptionResponse.builder().field("Error exception").message("Unauthorized").build());
        } catch (DuplicateKeyException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ExceptionResponse.builder().field("Error exception").message("Duplicate name currency").build());
        }
    }

    @DeleteMapping()
    public ResponseEntity<ExceptionResponse> deleteCurrency(
            @Valid @RequestBody DeleteCurrencyBody deleteCurrencyBody, BindingResult bindingResult) throws Exception {
        try {

            // Delete plan
            currencyService.deleteCurrency(deleteCurrencyBody.getCurrencyId());

            return ResponseEntity.status(HttpStatus.OK).body(ExceptionResponse.builder().field("Delete").message("Delete successful").build());
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ExceptionResponse.builder().field("Error exception").message("Unauthorized").build());
        } catch (InvalidInputException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ExceptionResponse.builder().field("Error exception").message(e.getMessage()).build());
        } catch (DuplicateKeyException | ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ExceptionResponse.builder().field("Error exception").message("Not found any currency have id = " + deleteCurrencyBody.getCurrencyId()).build());
        }
    }

    @PutMapping("/update")
    public ResponseEntity<ExceptionResponse> updateCurrency(
            @Valid @RequestBody UpdateCurrencyBody updateCurrencyBody, BindingResult bindingResult) throws Exception {

        if (bindingResult.hasErrors()) {
            // Xử lý lỗi validation và trả về phản hồi lỗi
            String errorMessage = bindingResult.getAllErrors().stream()
                    .map(ObjectError::getDefaultMessage)
                    .collect(Collectors.joining(", "));
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    ExceptionResponse.builder().field("Validation Error").message(errorMessage).build());
        }

        try {

            currencyService.updateCurrency(Currency.builder()
                    .id(updateCurrencyBody.getCurrencyId())
                    .name(updateCurrencyBody.getCurrencyName())
                    .symbol(updateCurrencyBody.getCurrencySymbol())
                    .affix(updateCurrencyBody.getCurrencyAffix())
                    .build());

            return ResponseEntity.status(HttpStatus.OK).body(ExceptionResponse.builder().field("Update").message("Update successful").build());
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ExceptionResponse.builder().field("Error exception").message("Unauthorized").build());
        } catch (DuplicateKeyException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ExceptionResponse.builder().field("Error exception").message("Duplicate name currency").build());
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ExceptionResponse.builder().field("Error exception").message("Not found any currency have id = " + updateCurrencyBody.getCurrencyId()).build());
        }
    }

    @GetMapping("/list")
    public ResponseEntity<ListResponse<CurrencyPaginateResponse>> getListCurrency() {

        // Get data
        List<Currency> currencies = currencyService.getListCurrency();

        // Response
        ListResponse<CurrencyPaginateResponse> responses = new ListResponse<>();

        if (currencies != null) {
            currencies.forEach(currency -> {
                responses.getData().add(CurrencyPaginateResponse.builder()
                        .currencyId(currency.getId())
                        .name(currency.getName())
                        .symbol(currency.getSymbol())
                        .affix(currency.getAffix())
                        .isDefault(currency.isDefault())
                        .createdAt(currency.getCreatedAt())
                        .updatedAt(currency.getUpdatedAt())
                        .build());
            });
        } else {

            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }

        return ResponseEntity.ok(responses);
    }
}
