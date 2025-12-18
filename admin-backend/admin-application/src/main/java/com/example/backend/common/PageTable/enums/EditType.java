package com.example.backend.common.PageTable.enums;

public enum EditType {
    CAN_NOT_EDIT("hidden"),
    PLAIN_TEXT("plainText"),
    INPUT("input"),
    INPUT_NUMBER("input-number"),
    TEXTAREA("textarea"),
    SELECT("select"),
    IMAGE("image"),
    // DATETIME("time"), // not implement yet
    DATE("date"),
    CHECKBOX("checkbox"),
    // 带候选项的输入框（需要配置 autoCompleteDropDown 属性）
    INPUT_AUTO_COMPLETE("autocomplete"),
    ;

    private final String value;

    EditType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
