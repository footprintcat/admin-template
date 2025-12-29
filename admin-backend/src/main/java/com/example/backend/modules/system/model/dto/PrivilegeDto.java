package com.example.backend.modules.system.model.dto;

import com.example.backend.common.utils.NumberUtils;
import com.example.backend.common.utils.StringUtils;
import com.example.backend.modules.system.model.entity.Privilege;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class PrivilegeDto {

    private String id;
    private Long roleId;
    private String module;
    private String type;

}
