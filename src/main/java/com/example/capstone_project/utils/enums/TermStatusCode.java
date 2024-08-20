package com.example.capstone_project.utils.enums;

import lombok.Getter;

@Getter
public enum TermStatusCode {

    IN_PROGRESS("IN PROGRESS"),
    NEW("NEW"),
    CLOSED("CLOSED");

    private final String value;

    TermStatusCode(String value) {
        this.value = value;
    }

}
