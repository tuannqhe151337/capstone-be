package com.example.capstone_project.controller;

import com.example.capstone_project.controller.responses.ListResponse;
import com.example.capstone_project.controller.responses.Pagination;
import com.example.capstone_project.controller.responses.expense.CostTypeResponse;
import com.example.capstone_project.controller.responses.expense.list.ExpenseResponse;
import com.example.capstone_project.controller.responses.plan.list.DepartmentResponse;
import com.example.capstone_project.controller.responses.plan.list.PlanResponse;
import com.example.capstone_project.controller.responses.plan.list.StatusResponse;
import com.example.capstone_project.controller.responses.plan.list.TermResponse;
import com.example.capstone_project.utils.helper.JwtHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/plan")
@RequiredArgsConstructor
public class FinancialPlanController {

    private final JwtHelper jwtHelper;
    @GetMapping("/list")
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

    @GetMapping("expenses")
    public ResponseEntity<ListResponse<ExpenseResponse>> getListExpense(
            @RequestParam(required = false) Integer termId,
            @RequestParam(required = false) Integer statusId,
            @RequestParam(required = false) Integer costTypeId,
            @RequestParam(required = false) String query,
            @RequestParam(required = false) String page,
            @RequestParam(required = false) String size,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String sortType
    ) {
        ListResponse<ExpenseResponse> listResponse = new ListResponse<>();
        listResponse.setData(List.of(
                ExpenseResponse.builder()
                        .expenseId(1L)
                        .name("Promotion event")
                        .costType(CostTypeResponse.builder()
                                .costTypeId(1L)
                                .name("Direct cost").build())
                        .unitPrice(BigDecimal.valueOf(15000000))
                        .amount(3)
                        .projectName("RECT")
                        .supplierName("Hong Ha")
                        .pic("HongHD9")
                        .notes("Approximate")
                        .status(com.example.capstone_project.controller.responses.expense.list.StatusResponse.builder()
                                .statusId(1L)
                                .name("Waiting for approval").build())
                        .build(),
                ExpenseResponse.builder()
                        .expenseId(2L)
                        .name("Social media")
                        .costType(CostTypeResponse.builder()
                                .costTypeId(1L)
                                .name("Direct cost").build())
                        .unitPrice(BigDecimal.valueOf(15000000))
                        .amount(1)
                        .projectName("IN22")
                        .supplierName("Hong Ha")
                        .pic("HongHD9")
                        .status(com.example.capstone_project.controller.responses.expense.list.StatusResponse.builder()
                                .statusId(2L)
                                .name("Waiting for approval").build())
                        .build(),
                ExpenseResponse.builder()
                        .expenseId(3L)
                        .name("Office supplier")
                        .costType(CostTypeResponse.builder()
                                .costTypeId(2L)
                                .name("Adminstration").build())
                        .unitPrice(BigDecimal.valueOf(5000000))
                        .amount(2)
                        .projectName("CAM1")
                        .supplierName("TuNM")
                        .pic("TuanVV")
                        .status(com.example.capstone_project.controller.responses.expense.list.StatusResponse.builder()
                                .statusId(1L)
                                .name("Waiting for approval").build())
                        .build()
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
