package com.example.backend.modules.system.model.dto;

import com.example.backend.modules.system.model.entity.Identity;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class IdentityDto {

    private Long id;
    private Long departmentId;
    private Long userId;
    private Long tenantId;

    public static IdentityDto fromEntity(Identity entity) {
        if (entity == null) {
            return null;
        }
        IdentityDto dto = new IdentityDto();
        BeanUtils.copyProperties(entity, dto);
        return dto;
    }

    public static List<IdentityDto> fromEntity(List<Identity> entityList) {
        return entityList.stream().map(IdentityDto::fromEntity).collect(Collectors.toList());
    }

    public static Identity toEntity(IdentityDto dto) {
        if (dto == null) {
            return null;
        }
        Identity entity = new Identity();
        BeanUtils.copyProperties(dto, entity);
        return entity;
    }
}
