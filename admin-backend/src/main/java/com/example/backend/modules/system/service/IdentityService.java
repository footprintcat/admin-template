package com.example.backend.modules.system.service;

import com.example.backend.common.error.BusinessErrorCode;
import com.example.backend.common.error.BusinessException;
import com.example.backend.common.utils.SessionUtils;
import com.example.backend.modules.system.model.converter.IdentityConverter;
import com.example.backend.modules.system.model.dto.IdentityDto;
import com.example.backend.modules.system.model.entity.Identity;
import com.example.backend.modules.system.repository.IdentityRepository;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class IdentityService {

    @Resource
    private IdentityConverter identityConverter;
    @Resource
    private IdentityRepository identityRepository;

    /**
     * 通过 userId 获取身份列表 (dto)
     *
     * @param userId 用户id
     * @return identityDto 身份列表
     * @since 2025-12-17
     */
    public @NotNull List<IdentityDto> getIdentityListByUserId(@NotNull Long userId) {
        List<Identity> identityList = identityRepository.getIdentityListByUserId(userId);
        return identityConverter.toDto(identityList);
    }

    /**
     * 通过 identityId 查询身份信息
     *
     * @param userId     用户id
     * @param identityId 身份id
     * @return IdentityDto
     * @since 2025-12-18
     */
    public @Nullable IdentityDto getUserIdentityDtoById(@NotNull Long userId, @Nullable Long identityId) {
        if (identityId == null) {
            return null;
        }
        @Nullable
        Identity identity = identityRepository.getUserIdentityId(userId, identityId);
        return identityConverter.toDto(identity);
    }

    /**
     * 切换用户身份
     *
     * @param session
     * @param userId     用户id
     * @param identityId 身份id
     * @throws BusinessException 业务异常
     * @since 2025-12-18
     */
    public void switchUserIdentity(@NotNull HttpSession session, @NotNull Long userId, @NotNull Long identityId) throws BusinessException {
        @Nullable
        Identity identity = identityRepository.getById(identityId);

        if (identity == null) {
            throw new BusinessException(BusinessErrorCode.INCORRECT_DATA, "身份切换失败，参数错误");
        }
        if (!Objects.equals(identity.getUserId(), userId)) {
            throw new BusinessException(BusinessErrorCode.ILLEGAL_CALL, "您无权切换到此身份");
        }
        SessionUtils.setSessionIdentityId(session, identity.getId());
    }

}
