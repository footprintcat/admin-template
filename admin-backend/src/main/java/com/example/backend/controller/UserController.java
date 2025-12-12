package com.example.backend.controller;

import com.alibaba.fastjson2.JSONObject;
import com.example.backend.common.Response.CommonReturnType;
import com.example.backend.common.Utils.IPUtils;
import com.example.backend.common.Utils.SessionUtils;
import com.example.backend.common.Utils.StringUtils;
import com.example.backend.controller.base.BaseController;
import com.example.backend.dto.SystemUserDTO;
import com.example.backend.entity.SystemLog;
import com.example.backend.entity.SystemUser;
import com.example.backend.service.System.SystemLogService;
import com.example.backend.service.System.UserService;
import com.example.backend.service.v2.SystemUserServiceV2;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Objects;

// TODO
@Deprecated
@CrossOrigin
@RestController
@RequestMapping("/v1/user")
public class UserController extends BaseController {

    // TODO
    private final Long SUPER_ADMIN_ROLE_ID = 1L;

    @Resource
    private UserService userService;
    @Resource
    private SystemUserServiceV2 systemUserServiceV2;
    @Resource
    private SystemLogService systemLogService;

    @PostMapping("/login")
    public CommonReturnType login(@RequestBody JSONObject params, HttpServletRequest httpServletRequest) {
        HttpSession session = httpServletRequest.getSession();

        // 获取用户输入
        String inputUsername = params.getString("userName");
        String inputPassword = params.getString("passWord");

        // 通过用户名查出用户信息
        // 此时尚未判断用户密码是否正确，在判断完成前，禁止访问该对象其他信息
        SystemUser userByUsername = systemUserServiceV2.getUserByUsername(inputUsername);
        SystemUserDTO systemUserDTO = null;

        // 判断密码是否正确
        boolean isCorrect = systemUserServiceV2.checkPasswordIsCorrect(userByUsername, inputPassword);
        if (isCorrect) {
            // 密码正确，登录成功
            systemUserDTO = SystemUserDTO.fromEntity(userByUsername);
            SessionUtils.setSession(session, userByUsername);
        }

        // 增加登录日志
        SystemLog systemLog = new SystemLog();
        systemLog.setAction("login");
        systemLog.setContent(isCorrect ? "登录成功" : "登录失败：密码错误");
        systemLog.setIp(IPUtils.getIpAddr(httpServletRequest));
        systemLog.setTitle("用户登录");
        systemLog.setUserId(Objects.isNull(userByUsername) ? null : userByUsername.getId());
        systemLogService.add(systemLog);

        return CommonReturnType.success(systemUserDTO);
    }

    @PostMapping("/getInfo")
    public CommonReturnType getUserInfo(HttpServletRequest httpServletRequest) {
        HttpSession session = httpServletRequest.getSession();
        // 未登录状态 map 为 null
        HashMap<String, Object> userInfoMap = SessionUtils.getUserInfoMap(session);
        return CommonReturnType.success(userInfoMap);
    }

    @PostMapping("/logout")
    public CommonReturnType logout(HttpServletRequest httpServletRequest) {
        HttpSession session = httpServletRequest.getSession();
        HashMap<String, Object> userInfoMap = SessionUtils.getUserInfoMap(session);

        // 增加登录日志
        SystemLog systemLog = new SystemLog();
        systemLog.setAction("logout");
        systemLog.setContent("登出成功");
        systemLog.setIp(IPUtils.getIpAddr(httpServletRequest));
        systemLog.setTitle("用户登出");
        if (Objects.nonNull(userInfoMap)) {
            Object userId = userInfoMap.get("id");
            if (Objects.nonNull(userId)) {
                systemLog.setUserId((Long) userId);
            }
        }
        systemLogService.add(systemLog);

        session.invalidate();
        return CommonReturnType.success();
    }

    @PostMapping("/alterPassword")
    public CommonReturnType alterPassword(@RequestBody JSONObject params) {
        Long userId = params.getLong("userId");
        String oldPassword = params.getString("oldPassword");
        String newPassword = params.getString("newPassword");

        userService.alterPassword(userId, oldPassword, newPassword);
        return CommonReturnType.success();
    }

    @PostMapping("/authenticate")
    public CommonReturnType authenticate(@RequestBody JSONObject params, HttpServletRequest httpServletRequest) {
        // 获取输入的密码
        String inputPassword = params.getString("password");
        if (StringUtils.isEmpty(inputPassword)) {
            return CommonReturnType.error("密码不能为空！");
        }

        // 获取当前用户username
        SystemUser userByHttpServlet = systemUserServiceV2.getCurrentLoginUser(httpServletRequest);

        // 判断密码是否正确
        boolean isCorrect = systemUserServiceV2.checkPasswordIsCorrect(userByHttpServlet, inputPassword);

        if (!isCorrect) {
            return CommonReturnType.error("密码有误！请重输");
        }
        // 密码正确 校验是否为超管用户
        Long roleId = userByHttpServlet.getRoleId();
        if (!SUPER_ADMIN_ROLE_ID.equals(roleId)) {
            return CommonReturnType.error("该用户权限不足！");
        }

        return CommonReturnType.success(true);
    }
}
