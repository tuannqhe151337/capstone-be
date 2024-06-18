package com.example.capstone_project.utils.exception.department;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class InvalidDepartmentIdException extends   Exception{
    public InvalidDepartmentIdException(String message){
        super(message);
    }
}
