package com.example.capstone_project.utils.helper;

import com.example.capstone_project.entity.CostType;
import com.example.capstone_project.entity.Department;
import com.example.capstone_project.repository.result.ExpenseResult;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Component
public class HandleFileHelper {
    public byte[] fillDataToExcel(Workbook wb, List<ExpenseResult> expenses, List<Department> departments, List<CostType> costTypes) throws IOException {
        Sheet sheet = wb.getSheet("Expense");

        Row row = null;

        int rowPosition = 2;
        int colPosition;

        for (ExpenseResult expenseResult : expenses) {
            colPosition = 0;
            if (sheet.getRow(rowPosition) == null)
                row = sheet.createRow(rowPosition++);
            else row = sheet.getRow(rowPosition++);
            row.getCell(colPosition++).setCellValue(expenseResult.getExpenseCode());
            row.getCell(colPosition++).setCellValue(expenseResult.getDate().toLocalDate());
            row.getCell(colPosition++).setCellValue(expenseResult.getTermName());
            row.getCell(colPosition++).setCellValue(expenseResult.getDepartmentName());
            row.getCell(colPosition++).setCellValue(expenseResult.getExpenseName());
            row.getCell(colPosition++).setCellValue(expenseResult.getCostTypeName());
            row.getCell(colPosition++).setCellValue(expenseResult.getUnitPrice().doubleValue());
            row.getCell(colPosition++).setCellValue(expenseResult.getAmount());
            row.getCell(colPosition++).setCellValue(expenseResult.getTotal().doubleValue());
            row.getCell(colPosition++).setCellValue(expenseResult.getProjectName());
            row.getCell(colPosition++).setCellValue(expenseResult.getSupplierName());
            row.getCell(colPosition++).setCellValue(expenseResult.getPic());
            row.getCell(colPosition++).setCellValue(expenseResult.getNote());
            row.getCell(colPosition).setCellValue(expenseResult.getStatusCode().toString());
        }

        sheet = wb.getSheet("List");


        // Write department
        rowPosition = 2;

        for (Department department : departments) {
            colPosition = 0;
            if (sheet.getRow(rowPosition) == null)
                row = sheet.createRow(rowPosition++);
            else row = sheet.getRow(rowPosition++);

            row.createCell(colPosition++).setCellValue(department.getId());
            row.createCell(colPosition).setCellValue(department.getName());
        }

        // Write cost type
        rowPosition = 2;

        for (CostType costType : costTypes) {
            colPosition = 3;
            if (sheet.getRow(rowPosition) == null)
                row = sheet.createRow(rowPosition++);
            else row = sheet.getRow(rowPosition++);
            row.createCell(colPosition++).setCellValue(costType.getId());
            row.createCell(colPosition).setCellValue(costType.getName());
        }

        // Add validation
        sheet = wb.getSheet("Expense");
        // Add validation for department

        DataValidationHelper validationHelper = sheet.getDataValidationHelper();
        DataValidationConstraint constraint = validationHelper.createFormulaListConstraint("List!$B$3:$B$" + (departments.size() + 2));

        CellRangeAddressList addressList = new CellRangeAddressList(2, expenses.size() + 2, 3, 3);
        DataValidation dataValidation = validationHelper.createValidation(constraint, addressList);
        dataValidation.setShowErrorBox(true);

        sheet.addValidationData(dataValidation);

        // Add validation for department

        constraint = validationHelper.createFormulaListConstraint("List!$E$3:$E$" + (costTypes.size() + 2));

        addressList = new CellRangeAddressList(2, expenses.size() + 2, 5, 5);
        dataValidation = validationHelper.createValidation(constraint, addressList);
        dataValidation.setShowErrorBox(true);

        sheet.addValidationData(dataValidation);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        wb.write(out);
        wb.close();
        out.close();
        return out.toByteArray();
    }

}
