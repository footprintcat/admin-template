package com.example.backend.common.PageTable.enums;

public enum FieldType {
    HIDDEN("null"),
    TEXT("plaintext"),
    LONG_TEXT("longtext"),
    IMAGE("image"),
    DATETIME("time"),
    DATE("date"),
    TAG("tag"),
    BOOLEAN("checkbox"),
    ;

    private final String value;

    FieldType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
