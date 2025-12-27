package com.example.backend.common.utils;

import com.example.backend.common.error.BusinessErrorCode;
import com.example.backend.common.error.BusinessException;
import com.example.backend.modules.system.model.entity.User;
import jakarta.servlet.http.HttpSession;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SessionUtils {

    /**
     * 登录时设置 session
     *
     * @param session
     * @param userId
     */
    public static void setSessionUserId(@NotNull HttpSession session, @NotNull Long userId) {
        session.setAttribute("user_id", userId);
    }

    /**
     * 登录时设置 session
     * 调用前请确保 user != null
     *
     * @param session
     * @param user
     */
    public static void setSessionUserId(@NotNull HttpSession session, @NotNull User user) {
        session.setAttribute("user_id", user.getId());
    }

    /**
     * 切换身份
     *
     * @param session
     * @param identityId 身份id
     * @since 2025-12-18
     */
    public static void setSessionIdentityId(@NotNull HttpSession session, @NotNull Long identityId) {
        session.setAttribute("identity_id", identityId);
    }

    public static void logout(HttpSession session) {
        session.invalidate();
    }

    public static boolean isLogin(HttpSession session) {
        return session.getAttribute("user_id") != null;
    }

    public static @Nullable Long getUserId(HttpSession session) {
        return getLong(session, "user_id");
    }

    public static @NotNull Long getUserIdOrThrow(HttpSession session) throws BusinessException {
        @Nullable Long userId = getLong(session, "user_id");
        if (userId == null) {
            throw new BusinessException(BusinessErrorCode.USER_NOT_LOGIN);
        }
        return userId;
    }

    public static @Nullable Long getIdentityId(HttpSession session) {
        return getLong(session, "identity_id");
    }

    public static @NotNull Long getIdentityIdOrThrow(HttpSession session) throws BusinessException {
        @Nullable Long identityId = getLong(session, "identity_id");
        if (identityId == null) {
            throw new BusinessException(BusinessErrorCode.USER_NOT_SELECT_IDENTITY);
        }
        return identityId;
    }

    public static void clearIdentityId(HttpSession session) {
        session.removeAttribute("identity_id");
    }

    private static @Nullable Integer getInteger(HttpSession session, String attrName) {
        try {
            String str = session.getAttribute(attrName).toString();
            if (str == null) {
                return null;
            }
            return Integer.parseInt(str);
        } catch (Exception e) {
            return null;
        }
    }

    private static @Nullable Long getLong(HttpSession session, String attrName) {
        try {
            String str = session.getAttribute(attrName).toString();
            if (str == null) {
                return null;
            }
            return Long.parseLong(str);
        } catch (Exception e) {
            return null;
        }
    }
}
