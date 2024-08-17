package com.example.capstone_project.utils.enums;

import lombok.Getter;

@Getter
public enum TermCode {

    IN_PROGRESS("IN PROGRESS"),
    NEW("NEW"),
    CLOSED("CLOSED");

    private String value;

    TermCode(String value) {
        this.value = value;
    }

}
