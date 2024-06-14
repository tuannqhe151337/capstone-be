package com.example.capstone_project.controller;

import com.example.capstone_project.controller.responses.Responses;
import com.example.capstone_project.controller.responses.expense.CostTypeResponse;
import com.example.capstone_project.entity.AccessTokenClaim;
import com.example.capstone_project.entity.CostType;
import com.example.capstone_project.service.CostTypeService;
import com.example.capstone_project.utils.helper.JwtHelper;
import com.example.capstone_project.utils.mapper.expense.CostTypeMapperImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cost-type")
@RequiredArgsConstructor
public class CostTypeController {
    private final CostTypeService costTypeService;

    @GetMapping("/list")
    public ResponseEntity<Responses<CostTypeResponse>> getListCostType() {

        // Get data
        List<CostType> costTypes = costTypeService.getListCostType();

        // Response
        Responses<CostTypeResponse> responses = new Responses<>();

        if (costTypes != null) {

            // Mapping to CostTypeResponse
            responses.setData(costTypes.stream().map(x -> {
                return new CostTypeMapperImpl().mapToCostTypeResponse(x);
            }).toList());
        } else {

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        return ResponseEntity.ok(responses);
    }

}
