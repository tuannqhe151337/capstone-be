package com.example.capstone_project.utils.exception.postion;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class InvalidPositiontIdException extends Exception{
    public InvalidPositiontIdException(String message){
        super(message);
    }
}