package com.example.capstone_project.controller;

import com.example.capstone_project.controller.body.exchange.DeleteExchangeBody;
import com.example.capstone_project.controller.body.exchange.CreateExchangeBody;
import com.example.capstone_project.controller.body.exchange.UpdateExchangeBody;
import com.example.capstone_project.controller.responses.ExceptionResponse;
import com.example.capstone_project.controller.responses.ListPaginationResponse;
import com.example.capstone_project.controller.responses.Pagination;
import com.example.capstone_project.controller.responses.exchange.CurrencyResponse;
import com.example.capstone_project.controller.responses.exchange.ExchangeRateResponse;
import com.example.capstone_project.controller.responses.exchange.MonthExchangeRateResponse;
import com.example.capstone_project.entity.Currency;
import com.example.capstone_project.entity.CurrencyExchangeRate;
import com.example.capstone_project.service.CurrencyExchangeRateService;
import com.example.capstone_project.service.result.ExchangeResult;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/exchange")
@RequiredArgsConstructor
public class CurrencyExchangeRateController {
    private final CurrencyExchangeRateService exchangeService;

    @GetMapping("/list-paginate")
    public ResponseEntity<ListPaginationResponse<MonthExchangeRateResponse>> getListExchangePaging(
            @RequestParam(required = false) Integer year,
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

            if (query == null) {
                query = "";
            }

            // Handling pagination
            Pageable pageable = PaginationHelper.handlingPagination(pageInt, sizeInt, sortBy, sortType);

            // Get data
            TreeMap<String, List<ExchangeResult>> exchanges = exchangeService.getListExchangePaging(query, year, pageable);

            // Response
            ListPaginationResponse<MonthExchangeRateResponse> response = new ListPaginationResponse<>();

            long count = 0;

            if (exchanges != null) {

                // Count total record
                count = exchangeService.countDistinctListExchangePaging(year, pageable);

                exchanges.forEach((month, exchangeResults) -> {
                    response.getData().add(MonthExchangeRateResponse.builder()
                            .month(month)
                            .exchangeRates(exchangeResults.stream().map(exchangeResult -> {
                                return ExchangeRateResponse.builder()
                                        .amount(exchangeResult.getAmount())
                                        .currency(CurrencyResponse.builder()
                                                .currencyId(exchangeResult.getCurrency().getId())
                                                .name(exchangeResult.getCurrency().getName())
                                                .symbol(exchangeResult.getCurrency().getSymbol())
                                                .affix(exchangeResult.getCurrency().getAffix())
                                                .build())
                                        .build();
                            }).toList())
                            .build());
                });

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
    public ResponseEntity<ExceptionResponse> createExchange(
            @Valid @RequestBody CreateExchangeBody createExchangeBody, BindingResult bindingResult) throws Exception {
        if (bindingResult.hasErrors()) {
            // Xử lý lỗi validation và trả về phản hồi lỗi
            String errorMessage = bindingResult.getAllErrors().stream()
                    .map(ObjectError::getDefaultMessage)
                    .collect(Collectors.joining(", "));
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    ExceptionResponse.builder().field("Validation Error").message(errorMessage).build());
        }

        try {
            List<CurrencyExchangeRate> exchangeRates = new ArrayList<>();
            createExchangeBody.getExchangeRates().forEach(exchangeRate ->
            {
                exchangeRates.add(CurrencyExchangeRate.builder()
                        .month(createExchangeBody.getMonth())
                        .currency(Currency.builder()
                                .id(exchangeRate.getCurrencyId())
                                .build())
                        .amount(exchangeRate.getAmount())
                        .build());
            });
            exchangeService.createExchange(exchangeRates);

            return ResponseEntity.status(HttpStatus.CREATED).body(ExceptionResponse.builder().field("Create").message("Create successful").build());
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ExceptionResponse.builder().field("Error exception").message("Unauthorized").build());
        } catch (DuplicateKeyException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ExceptionResponse.builder().field("Error exception").message("Duplicate name exchange").build());
        }
    }

    @DeleteMapping()
    public ResponseEntity<ExceptionResponse> deleteExchange(
            @Valid @RequestBody DeleteExchangeBody deleteExchangeBody, BindingResult bindingResult) throws Exception {
        try {

            // Delete plan
            exchangeService.deleteExchange(deleteExchangeBody.getExchangeId());

            return ResponseEntity.status(HttpStatus.OK).body(ExceptionResponse.builder().field("Delete").message("Delete successful").build());
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ExceptionResponse.builder().field("Error exception").message("Unauthorized").build());
        } catch (DuplicateKeyException | ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ExceptionResponse.builder().field("Error exception").message("Not found any exchange have id = " + deleteExchangeBody.getExchangeId()).build());
        }
    }

    @PutMapping("/update")
    public ResponseEntity<ExceptionResponse> updateExchange(
            @Valid @RequestBody UpdateExchangeBody updateExchangeBody, BindingResult bindingResult) throws Exception {

        if (bindingResult.hasErrors()) {
            // Xử lý lỗi validation và trả về phản hồi lỗi
            String errorMessage = bindingResult.getAllErrors().stream()
                    .map(ObjectError::getDefaultMessage)
                    .collect(Collectors.joining(", "));
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    ExceptionResponse.builder().field("Validation Error").message(errorMessage).build());
        }

        try {

            exchangeService.updateExchange(CurrencyExchangeRate.builder()
                    .id(updateExchangeBody.getExchangeId())
                    .amount(updateExchangeBody.getAmount()).build());

            return ResponseEntity.status(HttpStatus.OK).body(ExceptionResponse.builder().field("Update").message("Update successful").build());
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ExceptionResponse.builder().field("Error exception").message("Unauthorized").build());
        } catch (DuplicateKeyException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ExceptionResponse.builder().field("Error exception").message("Duplicate name exchange").build());
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ExceptionResponse.builder().field("Error exception").message("Not found any exchange have id = " + updateExchangeBody.getExchangeId()).build());
        }
    }

}
