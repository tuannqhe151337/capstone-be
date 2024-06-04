package com.example.capstone_project.utils.enums;

public enum TermStatusName {

    IN_PROGRESS("IN PROGRESS"),
    NOT_STARTED("NOT STARTED"),
    CLOSED("CLOSED");

    private String value;

    TermStatusName(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

}
