package exceptionhandler;

import com.example.capstone_project.config.GlobalExceptionHandler;
import com.example.capstone_project.controller.responses.ExceptionResponse;

import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler globalExceptionHandler;

    @BeforeEach
    void setUp() {
        globalExceptionHandler = new GlobalExceptionHandler();
    }

    @Test
    void testHandleConstraintViolationException() {
        // Mocking ConstraintViolationException
        String exceptionMessage = "field1: Must not be empty, field2: Size must be between 5 and 10";
        ConstraintViolationException ex = new ConstraintViolationException(exceptionMessage, null);

        // Call the method
        ResponseEntity<List<ExceptionResponse>> response = globalExceptionHandler.handleConstraintViolationException(ex, null);

        // Assertions
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        List<ExceptionResponse> responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals(2, responseBody.size());
        assertEquals("field1", responseBody.get(0).getField());
        assertEquals("Must not be empty", responseBody.get(0).getMessage());
        assertEquals("field2", responseBody.get(1).getField());
        assertEquals("Size must be between 5 and 10", responseBody.get(1).getMessage());
    }
    @Test
    void testHandleHttpMessageNotReadableException() {

        HttpMessageNotReadableException ex = new HttpMessageNotReadableException("Json parse error: Invalid JSON format  ");

        ResponseEntity<List<ExceptionResponse>> responseEntity = globalExceptionHandler.handleConstraintViolationException(ex, null);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        List<ExceptionResponse> body = responseEntity.getBody();
        assertEquals(1, body.size());
        assertEquals("Json parse error", body.get(0).getField());
        assertEquals("Invalid JSON format", body.get(0).getMessage());
    }

    @Test
    void testHandleMethodArgumentNotValidException() {
        // Mocking MethodArgumentNotValidException
        Object[] errors = {"kkkkkkkkk", "kkkkkkkkk"};
        MethodArgumentNotValidException ex = mock(MethodArgumentNotValidException.class);
        when(ex.getDetailMessageArguments()).thenReturn(errors);

        // Call the method
        ResponseEntity<List<ExceptionResponse>> response = globalExceptionHandler.handleMethodArgumentNotValidException(ex, null);

        // Assertions
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        List<ExceptionResponse> responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals(2, responseBody.size());
        assertEquals("fullName", responseBody.get(0).getField());
        assertEquals("Full name cannot be empty", responseBody.get(0).getMessage());
        assertEquals("dob", responseBody.get(1).getField());
        assertEquals("Date of birth must be in the past", responseBody.get(1).getMessage());
    }



}
