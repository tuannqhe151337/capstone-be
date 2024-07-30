package com.example.capstone_project.utils.enums;

import lombok.Getter;

@Getter
public enum AuthorityCode {
    CREATE_NEW_USER("A-001"),VIEW_LIST_USERS("A-002"),DELETE_USER("A-003"),EDIT_USER("A-004"),ACTIVATE_USER("A-005"),DEACTIVATE_USER("A-006"), VIEW_USER_DETAILS("A-007"),
    CREATE_TERM("B-001"),EDIT_TERM("B-002"),VIEW_TERM("B-003"),START_TERM("B-004"),DELETE_TERM("B-005"),
    IMPORT_PLAN("C-001"), RE_UPLOAD_PLAN("C-002"),SUBMIT_PLAN_FOR_REVIEW("C-003"),DELETE_PLAN("C-004"),DOWNLOAD_PLAN("C-005"),APPROVE_PLAN("C-006"),VIEW_PLAN("C-007"),
    VIEW_REPORT("D-001"),DOWNLOAD_REPORT("D-002"),DELETE_REPORT("D-003"),
    VIEW_ANNUAL_REPORT("E-001"),DOWNLOAD_ANNUAL_REPORT("E-002");

    private final String value;

    AuthorityCode(String value) {
        this.value = value;
    }

}