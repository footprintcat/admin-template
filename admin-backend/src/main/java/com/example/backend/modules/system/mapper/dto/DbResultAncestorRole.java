package com.example.backend.modules.system.mapper.dto;

import lombok.Data;

@Data
public class DbResultAncestorRole {

    private Long id;
    private String roleName;
    private Long parentId;
    private Long level;

    private Integer deps;
    private String path;

}
