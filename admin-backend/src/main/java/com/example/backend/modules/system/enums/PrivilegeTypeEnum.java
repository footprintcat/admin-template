package com.example.backend.modules.system.enums;

import lombok.Getter;

import java.util.HashMap;
import java.util.Objects;

@Getter
public enum PrivilegeTypeEnum {
    GRANTED("granted", "有权"),
    DENIED("denied", "无权"),
    INHERITABLE("inheritable", "有权继承"),
    ;
    private final String code;
    private final String name;

    PrivilegeTypeEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public static PrivilegeTypeEnum getByCode(String code) {
        PrivilegeTypeEnum[] values = PrivilegeTypeEnum.values();
        for (PrivilegeTypeEnum privilegeTypeEnum : values) {
            if (Objects.equals(privilegeTypeEnum.code, code)) {
                return privilegeTypeEnum;
            }
        }
        return null;
    }

    public static HashMap<String, String> getDropdown() {
        PrivilegeTypeEnum[] values = PrivilegeTypeEnum.values();
        HashMap<String, String> map = new HashMap<>();
        for (PrivilegeTypeEnum privilegeTypeEnum : values) {
            map.put(privilegeTypeEnum.getCode(), privilegeTypeEnum.getName());
        }
        return map;
    }
}
