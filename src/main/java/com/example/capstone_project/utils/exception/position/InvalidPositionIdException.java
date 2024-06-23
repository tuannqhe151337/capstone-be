package com.example.capstone_project.utils.exception.position;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class InvalidPositionIdException extends Exception{
    public InvalidPositionIdException(String message){
        super(message);
    }
}
