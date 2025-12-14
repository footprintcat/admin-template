package com.example.backend.common.utils;

import com.example.backend.common.error.BusinessErrorCode;
import com.example.backend.common.error.BusinessException;
import com.example.backend.modules.system.model.entity.SystemUser;
import jakarta.servlet.http.HttpSession;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SessionUtils {

    /**
     * 登录时设置 session
     *
     * @param session
     * @param username
     * @param userId
     * @param roleId
     */
    public static void setSession(@NotNull HttpSession session, @NotNull String username, @NotNull Long userId) {
        session.setAttribute("username", username);
        session.setAttribute("user_id", userId);
    }

    /**
     * 登录时设置 session
     * 调用前请确保 user != null
     *
     * @param session
     * @param systemUser
     */
    public static void setSession(@NotNull HttpSession session, @NotNull SystemUser systemUser) {
        session.setAttribute("username", systemUser.getUsername());
        session.setAttribute("user_id", systemUser.getId());
    }

    public static void logout(HttpSession session) {
        session.invalidate();
    }

    public static boolean isLogin(HttpSession session) {
        return session.getAttribute("username") != null;
    }

    public static @Nullable String getUsername(HttpSession session) {
        return session.getAttribute("username").toString();
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
