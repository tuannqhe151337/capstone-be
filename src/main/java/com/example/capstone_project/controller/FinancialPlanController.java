package com.example.capstone_project.controller;

import com.example.capstone_project.controller.body.plan.create.NewPlanBody;
import com.example.capstone_project.controller.responses.ListResponse;
import com.example.capstone_project.controller.responses.Pagination;
import com.example.capstone_project.controller.responses.planManagement.DepartmentResponse;
import com.example.capstone_project.controller.responses.planManagement.PlanResponse;
import com.example.capstone_project.controller.responses.planManagement.StatusResponse;
import com.example.capstone_project.controller.responses.planManagement.TermResponse;
import com.example.capstone_project.entity.AccessTokenClaim;
import com.example.capstone_project.utils.helper.JwtHelper;
import com.example.capstone_project.utils.mapper.body.plan.create.CreatePlanMapperImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/plan")
@RequiredArgsConstructor
public class FinancialPlanController {

    private final JwtHelper jwtHelper;

    @PostMapping("/create")
    public ResponseEntity<NewPlanBody> confirmExpenses(
            @RequestHeader("Authorization") String token,
            @RequestBody NewPlanBody body) {
        //Get access token
        final String accessToken = token.substring(7);

        //Get department ID
        AccessTokenClaim accessTokenClaim = jwtHelper.parseToken(accessToken);

        System.out.println(new CreatePlanMapperImpl().newPlanBodyToPlanMapping(body));

        return ResponseEntity.status(HttpStatus.CREATED).body(null);
    }

    @GetMapping("list")
    public ResponseEntity<ListResponse<PlanResponse>> getListPlan(
            @RequestParam(required = false) Integer termId,
            @RequestParam(required = false) Integer departmentId,
            @RequestParam(required = false) Integer statusId,
            @RequestParam(required = false) String query,
            @RequestParam(required = false) String page,
            @RequestParam(required = false) String size,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String sortType
    ){
        ListResponse<PlanResponse> listResponse = new ListResponse<>();
        listResponse.setData(List.of(
                PlanResponse.builder()
                        .id(1L)
                        .name("BU name_term_plan")
                        .status(StatusResponse.builder()
                                .statusId(1L)
                                .name("New").build())
                        .term(TermResponse.builder()
                                .termId(1L)
                                .name("Term name 1").build())
                        .department(DepartmentResponse.builder()
                                .departmentId(1L)
                                .name("BU 1").build())
                        .version("V1").build(),
                PlanResponse.builder()
                        .id(2L)
                        .name("BU name_term_plan")
                        .status(StatusResponse.builder()
                                .statusId(2L)
                                .name("Approved").build())
                        .term(TermResponse.builder()
                                .termId(1L)
                                .name("Term name 1").build())
                        .department(DepartmentResponse.builder()
                                .departmentId(2L)
                                .name("BU 2").build())
                        .version("V2").build()
                ));

        listResponse.setPagination(Pagination.builder()
                .count(100)
                .page(10)
                .displayRecord(0)
                .numPages(1)
                .build());

        return ResponseEntity.ok(listResponse);
    }
}
