package com.example.backend.modules.system.model.dto;

import lombok.Data;

@Data
public class IdentityDto {

    private Long id;
    private Long departmentId;
    private Long userId;
    private String name;
    private String intro;
    private Long tenantId;

}
