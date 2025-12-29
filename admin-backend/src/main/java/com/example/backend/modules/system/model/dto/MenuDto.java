package com.example.backend.modules.system.model.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class MenuDto implements Serializable {

    private String id;
    private String parentId;
    private Integer level;
    private String menuType;
    private String menuCode;
    private String actionCode;
    private String menuName;
    private String menuPath;
    private Integer sortOrder;
    private Boolean canEdit;
    private Boolean isHide;
    private List<MenuDto> children;

}
