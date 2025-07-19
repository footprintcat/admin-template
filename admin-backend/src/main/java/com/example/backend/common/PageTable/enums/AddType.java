package com.example.backend.common.PageTable.enums;

public enum AddType {
    CAN_NOT_ADD("hidden"),
    PLAIN_TEXT("plainText"),
    INPUT("input"),
    INPUT_NUMBER("input-number"),
    TEXTAREA("textarea"),
    SELECT("select"),
    IMAGE("image"),
    // DATETIME("time"), // not implement yet
    DATE("date"),
    CHECKBOX("checkbox"),
    // 带候选项的输入框（需要配置）
    INPUT_AUTO_COMPLETE("autocomplete"),
    ;

    private final String value;

    AddType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
