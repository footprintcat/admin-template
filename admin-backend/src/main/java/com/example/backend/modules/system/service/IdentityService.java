package com.example.backend.modules.system.service;

import com.example.backend.modules.system.model.dto.IdentityDto;
import com.example.backend.modules.system.model.entity.Identity;
import com.example.backend.modules.system.repository.IdentityRepository;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class IdentityService {

    @Resource
    private IdentityRepository identityRepository;

    /**
     * 通过 userId 获取身份列表 (dto)
     *
     * @param userId 用户id
     * @return identityDto 身份列表
     * @since 2025-12-17
     */
    public List<IdentityDto> getIdentityListByUserId(@NotNull Long userId) {
        List<Identity> identityList = identityRepository.getIdentityListByUserId(userId);
        return IdentityDto.fromEntity(identityList);
    }

}
