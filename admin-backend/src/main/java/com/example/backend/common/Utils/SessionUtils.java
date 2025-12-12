package com.example.backend.common.Utils;

import com.example.backend.entity.SystemUser;
import jakarta.servlet.http.HttpSession;

import java.util.HashMap;

public class SessionUtils {

    /**
     * 登录时设置 session
     *
     * @param session
     * @param username
     * @param userId
     * @param roleId
     */
    public static void setSession(HttpSession session, String username, Integer userId, Long roleId) {
        session.setAttribute("username", username);
        session.setAttribute("user_id", userId);
        session.setAttribute("role_id", roleId);
    }

    /**
     * 登录时设置 session
     * 调用前请确保 user != null
     *
     * @param session
     * @param systemUser
     */
    public static void setSession(HttpSession session, SystemUser systemUser) {
        session.setAttribute("username", systemUser.getUsername());
        session.setAttribute("user_id", systemUser.getId());
        session.setAttribute("role_id", systemUser.getRoleId());
    }

    public static boolean isLogin(HttpSession session) {
        return session.getAttribute("username") != null;
    }

    public static HashMap<String, Object> getUserInfoMap(HttpSession session) {
        if (!isLogin(session)) {
            return null;
        }
        HashMap<String, Object> map = new HashMap<>();
        map.put("username", getUsername(session));
        map.put("id", getUserId(session));
        map.put("idStr", String.valueOf(getUserId(session)));
        map.put("roleId", getRoleId(session));
        return map;
    }

    public static String getUsername(HttpSession session) {
        String username = session.getAttribute("username").toString();
        return username;
    }

    public static Long getUserId(HttpSession session) {
        Long userId = getLong(session, "user_id");
        return userId;
    }

    public static Long getRoleId(HttpSession session) {
        Long roleId = getLong(session, "role_id");
        return roleId;
    }

    private static Integer getInteger(HttpSession session, String attrName) {
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

    private static Long getLong(HttpSession session, String attrName) {
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
