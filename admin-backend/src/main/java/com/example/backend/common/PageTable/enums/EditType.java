package com.example.backend.common.PageTable.enums;

public enum EditType {
    CAN_NOT_EDIT("plainText"),
    INPUT("input"),
    INPUT_NUMBER("input-number"),
    TEXTAREA("textarea"),
    SELECT("select"),
    IMAGE("image"),
    // DATETIME("time"), // not implement yet
    DATE("date"),
    CHECKBOX("checkbox");

    private final String value;

    EditType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
