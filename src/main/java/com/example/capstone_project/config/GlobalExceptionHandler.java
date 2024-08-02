package com.example.capstone_project.config;

import com.example.capstone_project.controller.responses.ExceptionResponse;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.sql.SQLException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({SQLException.class})
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<List<ExceptionResponse>> handleSQLException(Exception ex, WebRequest request) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }

    /* ConstraintViolationException đối tượng được xác thực bằng Bean Validation API,
    nhưng không liên quan đến các tham số  của phương thức controller.
    Nó thường xảy ra trong các tình huống khác
    như khi bạn xác thực các đối tượng bằng cách sử dụng Validator API của Bean Validation.  */


    /* HttpMessageNotReadableException là một ngoại lệ trong Spring Framework xảy ra
     khi một HTTP request không thể được đọc hoặc phân tích (parse) thành đối tượng mục tiêu.
     Điều này thường xảy ra khi có lỗi trong quá trình chuyển đổi
    dữ liệu từ định dạng của request (ví dụ: JSON, XML) thành đối tượng Java.
    Json parse error: Can not de.....
    */


    @ExceptionHandler({ConstraintViolationException.class, HttpMessageNotReadableException.class})
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ResponseEntity<List<ExceptionResponse>> handleConstraintViolationException(Exception ex, WebRequest request) {
        String exceptionMessage = ex.getMessage();
        List<ExceptionResponse> exceptionResponseList = this.parseMessageToListExceptionResponse(exceptionMessage);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exceptionResponseList);
    }

    /* MethodArgumentNotValidException được ném ra khi có lỗi xác thực trong các tham số
    của phương thức của controller. Nó thường xảy ra khi bạn sử dụng các annotation xác thực
    từ javax.validation.constraints (như @NotNull, @Size, @Email, v.v.) trên
    các đối tượng được gửi qua yêu cầu HTTP (như đối tượng JSON trong thân yêu cầu).  */

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ResponseEntity<List<ExceptionResponse>> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex, WebRequest request) {
        Object[] errors = ex.getDetailMessageArguments();

        List<ExceptionResponse> exceptionResponseList = new ArrayList<>();

        if (errors != null) {
            for (Object error: errors) {
                if (error instanceof String s) {
                    String[] parts = s.trim().split(": ");
                    if (parts.length == 2) {
                        String fieldName = parts[0];
                        String errorMessageText = parts[1];
                        ExceptionResponse exception = ExceptionResponse.builder()
                                .field(fieldName).message(errorMessageText)
                                .build();
                        exceptionResponseList.add(exception);
                    }
                }
            }
        }


        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exceptionResponseList);
    }


//        @ExceptionHandler(HttpMessageNotReadableException.class)
//        @ResponseStatus(value = HttpStatus.BAD_REQUEST)
//        public ResponseEntity<List<ExceptionResponse>> handleHttpMessageNotReadableException(Exception ex, WebRequest request) {
//            String exceptionMessage = ex.getMessage();
//            List<ExceptionResponse> exceptionResponseList = this.parseMessageToListExceptionResponse(exceptionMessage);
//
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exceptionResponseList);
//        }

//    private List<ExceptionResponse> parseMessageToListExceptionResponse(String errorMessage) {
//        String[] errors = errorMessage.split(",");
//        List<ExceptionResponse> exceptionResponseList = new ArrayList<>();
//        for (String s : errors) {
//            String[] parts = s.trim().split(": ");
//            if (parts.length == 2) {
//                String fieldName = parts[0].substring(parts[0].lastIndexOf(".") + 1);
//                String errorMessageText = parts[1];
//                ExceptionResponse exception = ExceptionResponse.builder()
//                        .field(fieldName).message(errorMessageText)
//                        .build();
//                exceptionResponseList.add(exception);
//            }
//        }
//
//        return exceptionResponseList;
//    }
private List<ExceptionResponse> parseMessageToListExceptionResponse(String errorMessage) {
    List<ExceptionResponse> errorList = new ArrayList<>();
    // Biểu thức chính quy để tìm field và message
    Pattern pattern = Pattern.compile("(\\w+\\.\\w+\\.\\w+):\\s(.*?)(?=(?:\\w+\\.\\w+\\.\\w+:\\s|$))");
    Matcher matcher = pattern.matcher(errorMessage);

    while (matcher.find()) {
        String field = matcher.group(1).substring(matcher.group(1).lastIndexOf('.') + 1);
        String message = matcher.group(2).trim(); // Loại bỏ khoảng trắng và dấu phẩy thừa
        if (message.endsWith(",")) {
            message = message.substring(0, message.length() - 1).trim(); // Loại bỏ dấu phẩy thừa nếu có
        }
        errorList.add(new ExceptionResponse(field, message));
    }

    return errorList;
}

}

