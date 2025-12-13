package com.example.backend.common.Enums.system.privilege;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

@Getter
@AllArgsConstructor
public enum SystemPrivilegeEntityTypeEnum {

    USER("user", "用户"),
    ROLE("role", "角色"),
    ;

    @EnumValue
    @NotNull
    final String code;

    @NotNull
    final String name;

}
