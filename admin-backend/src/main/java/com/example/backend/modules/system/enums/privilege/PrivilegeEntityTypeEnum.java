package com.example.backend.modules.system.enums.privilege;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

@Getter
@AllArgsConstructor
public enum PrivilegeEntityTypeEnum {

    USER("user", "用户"),
    ROLE("role", "角色"),
    ;

    @EnumValue
    @NotNull
    final String code;

    @NotNull
    final String name;

}
