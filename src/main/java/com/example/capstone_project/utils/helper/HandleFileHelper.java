package com.example.capstone_project.utils.helper;

import com.example.capstone_project.entity.*;
import com.example.capstone_project.repository.result.ExpenseResult;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Component
public class HandleFileHelper {
    public byte[] fillDataToExcel(Workbook wb, List<ExpenseResult> expenses, List<Department> departments, List<CostType> costTypes, List<ExpenseStatus> expenseStatuses, List<Project> projects, List<Supplier> suppliers, List<Currency> currencies) throws IOException {
        Sheet sheet = wb.getSheet("Expense");

        Row row = null;

        int rowPosition = 2;
        int colPosition;

        for (ExpenseResult expenseResult : expenses) {
            colPosition = 0;
            if (sheet.getRow(rowPosition) == null)
                row = sheet.createRow(rowPosition++);
            else row = sheet.getRow(rowPosition++);
            row.getCell(colPosition++).setCellValue(expenseResult.getExpenseId());
            row.getCell(colPosition++).setCellValue(expenseResult.getExpenseCode());
            row.getCell(colPosition++).setCellValue(expenseResult.getDate().toLocalDate());
            row.getCell(colPosition++).setCellValue(expenseResult.getTermName());
            row.getCell(colPosition++).setCellValue(expenseResult.getDepartmentName());
            row.getCell(colPosition++).setCellValue(expenseResult.getExpenseName());
            row.getCell(colPosition++).setCellValue(expenseResult.getCostTypeName());
            row.getCell(colPosition++).setCellValue(expenseResult.getUnitPrice().doubleValue());
            row.getCell(colPosition++).setCellValue(expenseResult.getAmount());
            row.getCell(colPosition++).setCellValue(expenseResult.getTotal().doubleValue());
            row.getCell(colPosition++).setCellValue(expenseResult.getCurrencyName());
            row.getCell(colPosition++).setCellValue(expenseResult.getProjectName());
            row.getCell(colPosition++).setCellValue(expenseResult.getSupplierName());
            row.getCell(colPosition++).setCellValue(expenseResult.getPicName());
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

        // Write expense status
        rowPosition = 2;

        for (ExpenseStatus status : expenseStatuses) {
            colPosition = 6;
            if (sheet.getRow(rowPosition) == null)
                row = sheet.createRow(rowPosition++);
            else row = sheet.getRow(rowPosition++);
            row.createCell(colPosition++).setCellValue(status.getId());
            row.createCell(colPosition).setCellValue(status.getCode().toString());
        }

        // Write project
        rowPosition = 2;

        for (Project project : projects) {
            colPosition = 9;
            if (sheet.getRow(rowPosition) == null)
                row = sheet.createRow(rowPosition++);
            else row = sheet.getRow(rowPosition++);
            row.createCell(colPosition++).setCellValue(project.getId());
            row.createCell(colPosition).setCellValue(project.getName());
        }

        // Write supplier
        rowPosition = 2;

        for (Supplier supplier : suppliers) {
            colPosition = 12;
            if (sheet.getRow(rowPosition) == null)
                row = sheet.createRow(rowPosition++);
            else row = sheet.getRow(rowPosition++);
            row.createCell(colPosition++).setCellValue(supplier.getId());
            row.createCell(colPosition).setCellValue(supplier.getName());
        }

        // Write currency
        rowPosition = 2;

        for (Currency currency : currencies) {
            colPosition = 15;
            if (sheet.getRow(rowPosition) == null)
                row = sheet.createRow(rowPosition++);
            else row = sheet.getRow(rowPosition++);
            row.createCell(colPosition++).setCellValue(currency.getId());
            row.createCell(colPosition).setCellValue(currency.getName());
        }

        // Add validation
        sheet = wb.getSheet("Expense");
        // Add validation for department

        DataValidationHelper validationHelper = sheet.getDataValidationHelper();
        DataValidationConstraint constraint = validationHelper.createFormulaListConstraint("List!$B$3:$B$" + (departments.size() + 2));

        CellRangeAddressList addressList = new CellRangeAddressList(2, expenses.size() + 2, 4, 4);
        DataValidation dataValidation = validationHelper.createValidation(constraint, addressList);
        dataValidation.setShowErrorBox(true);

        sheet.addValidationData(dataValidation);

        // Add validation for cost type

        constraint = validationHelper.createFormulaListConstraint("List!$E$3:$E$" + (costTypes.size() + 2));

        addressList = new CellRangeAddressList(2, expenses.size() + 2, 6, 6);
        dataValidation = validationHelper.createValidation(constraint, addressList);
        dataValidation.setShowErrorBox(true);

        sheet.addValidationData(dataValidation);

        // Add validation for expense status

        constraint = validationHelper.createFormulaListConstraint("List!$H$3:$H$" + (expenseStatuses.size() + 2));

        addressList = new CellRangeAddressList(2, expenses.size() + 2, 15, 15);
        dataValidation = validationHelper.createValidation(constraint, addressList);
        dataValidation.setShowErrorBox(true);

        sheet.addValidationData(dataValidation);

        // Add validation for project

        constraint = validationHelper.createFormulaListConstraint("List!$K$3:$K$" + (projects.size() + 2));

        addressList = new CellRangeAddressList(2, expenses.size() + 2, 11, 11);
        dataValidation = validationHelper.createValidation(constraint, addressList);
        dataValidation.setShowErrorBox(true);

        sheet.addValidationData(dataValidation);

        // Add validation for supplier

        constraint = validationHelper.createFormulaListConstraint("List!$N$3:$N$" + (suppliers.size() + 2));

        addressList = new CellRangeAddressList(2, expenses.size() + 2, 12, 12);
        dataValidation = validationHelper.createValidation(constraint, addressList);
        dataValidation.setShowErrorBox(true);

        sheet.addValidationData(dataValidation);

        // Add validation for currency

        constraint = validationHelper.createFormulaListConstraint("List!$Q$3:$Q$" + (currencies.size() + 2));

        addressList = new CellRangeAddressList(2, expenses.size() + 2, 10, 10);
        dataValidation = validationHelper.createValidation(constraint, addressList);
        dataValidation.setShowErrorBox(true);

        sheet.addValidationData(dataValidation);

        // Back to default open is sheet expense
        sheet = wb.getSheet("Expense");
        int sheetIndex = wb.getSheetIndex(sheet);
        wb.setActiveSheet(sheetIndex);
        wb.setSelectedTab(sheetIndex);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        wb.write(out);
        wb.close();
        out.close();

        return out.toByteArray();
    }

}
