package com.example.backend.common.PageTable.enums;

public enum AddType {
    CAN_NOT_ADD("plainText"),
    INPUT("input"),
    INPUT_NUMBER("input-number"),
    TEXTAREA("textarea"),
    SELECT("select"),
    IMAGE("image"),
    // DATETIME("time"), // not implement yet
    DATE("date"),
    CHECKBOX("checkbox");

    private final String value;

    AddType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
