package com.example.backend.common.PageTable.enums;

public enum SearchType {
    CAN_NOT_SEARCH("null"),
    INPUT("input"),
    SELECT("select"),
    DATETIME_INTERVAL("time-interval"),
    CHECKBOX("checkbox");

    private final String value;

    SearchType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
