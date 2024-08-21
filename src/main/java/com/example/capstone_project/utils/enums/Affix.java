package com.example.capstone_project.utils.enums;

import lombok.Getter;

@Getter
public enum Affix {
    PREFIX("Prefix"), SUFFIX("Suffix");

    private final String value;

    Affix(String value) {
        this.value = value;
    }

}
