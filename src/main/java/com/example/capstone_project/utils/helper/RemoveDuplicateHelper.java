package com.example.capstone_project.utils.helper;

import com.example.capstone_project.controller.body.expense.ExpenseBody;

import java.util.*;
import java.util.stream.Collectors;

public class RemoveDuplicateHelper {
    public static List<Long> removeDuplicates(List<Long> inputList) {
        if (inputList == null) {
            return new ArrayList<>();
        }
        Set<Long> uniqueElements = new HashSet<>(inputList);
        return new ArrayList<>(uniqueElements);
    }

    public static List<ExpenseBody> removeDuplicateCodes(List<ExpenseBody> expenses) {
        Map<String, ExpenseBody> expenseMap = new HashMap<>();

        for (ExpenseBody expense : expenses) {
            expenseMap.put(expense.getExpenseCode(), expense);
        }

        return new ArrayList<>(expenseMap.values());
    }
}
