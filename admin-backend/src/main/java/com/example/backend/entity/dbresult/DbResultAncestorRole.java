package com.example.backend.entity.dbresult;

import lombok.Data;

@Data
public class DbResultAncestorRole {

    private Long id;
    private String roleName;
    private Long parentId;

    private Integer deps;
    private String path;

}
