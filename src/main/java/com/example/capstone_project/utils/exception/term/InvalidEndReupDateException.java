package com.example.capstone_project.utils.exception.term;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class InvalidEndReupDateException extends Exception{
    public InvalidEndReupDateException(String message) {
        super(message);
    }
}
