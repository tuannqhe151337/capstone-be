package com.example.capstone_project.utils.exception.role;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class InvalidRoleIdException extends Exception{
    public InvalidRoleIdException(String message){
        super(message);
    }
}
