package com.example.capstone_project.controller;


import com.example.capstone_project.controller.body.confirmExpenses.NewPlanBody;
import com.example.capstone_project.controller.responses.ListResponse;
import com.example.capstone_project.controller.responses.Pagination;
import com.example.capstone_project.controller.responses.expense.CostTypeResponse;
import com.example.capstone_project.controller.responses.expense.listExpenses.ExpenseResponse;
import com.example.capstone_project.controller.responses.expense.listExpenses.StatusResponse;
import com.example.capstone_project.entity.AccessTokenClaim;
import com.example.capstone_project.utils.helper.JwtHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/list-expense")
@RequiredArgsConstructor
public class ExpenseController {

}