package com.example.capstone_project.controller;

import com.example.capstone_project.controller.body.confirm_expenses.ConfirmNewExpenseBody;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/plan")
@RequiredArgsConstructor
public class ExpenseController {
    @PutMapping("/upload")
    public void selectTerm(
            @RequestBody(required = true) ConfirmNewExpenseBody body
    ) {
    }
}
