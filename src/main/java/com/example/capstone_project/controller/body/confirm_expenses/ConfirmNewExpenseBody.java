package com.example.capstone_project.controller.body.confirm_expenses;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ConfirmNewExpenseBody {
    private long termId;
    private String planName;
    private String fileName;
    private List<ExpenseBody> expenses;
}
