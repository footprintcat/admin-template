package com.example.backend.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.backend.entity.SystemLog;
import com.example.backend.entity.User;
import com.example.backend.mapper.SystemLogMapper;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Objects;

@Slf4j
@Service
public class SystemLogServiceBak extends ServiceImpl<SystemLogMapper, SystemLog> {

    public static void loginSetSession(HttpSession session, User user) {
        session.setAttribute("userId", String.valueOf(user.getId()));
        session.setAttribute("roleId", String.valueOf(user.getRoleId()));
        session.setAttribute("loginTime", System.currentTimeMillis());

        if (Objects.isNull(session.getAttribute("sessionId"))) {
            String sessionId = IdWorker.get32UUID();
            session.setAttribute("sessionId", sessionId);
        }
    }

    public void log(HttpSession session, String action, String apiPath, String userAgent, String remark) {
        SystemLog systemLog = new SystemLog();
        systemLog.setId(null);
        systemLog.setLogTime(new Date());
        systemLog.setAction(action);
        systemLog.setApiPath(apiPath);
        systemLog.setRemark(remark);
        systemLog.setUserAgent(userAgent);
        try {
            if (Objects.nonNull(session)) {
                String sessionId = session.getId();
                if ("1".equals(session.getAttribute("roleId"))) {
                    // 删除我自己的登录日志
                    this.remove(new LambdaQueryWrapper<SystemLog>()
                            .eq(SystemLog::getSessionId, sessionId));
                    log.info("登陆用户为超级管理员，跳过保存 SystemLog");
                    return;
                }

                systemLog.setSessionId(sessionId);
                if (Objects.nonNull(session.getAttribute("userId"))) { // 已登录
                    systemLog.setUserId((Long) session.getAttribute("userId"));
                }
                if (Objects.nonNull(session.getAttribute("roleId"))) { // 已登录
                    systemLog.setRoleId(session.getAttribute("roleId").toString());
                }
                if (Objects.nonNull(session.getAttribute("loginTime"))) {
                    String loginTime = session.getAttribute("loginTime").toString();
                    systemLog.setLoginTime(new Date(Long.parseLong(loginTime)));
                }
            }
        } catch (Exception ignored) {

        } finally {

        }
        this.save(systemLog);
    }
}
