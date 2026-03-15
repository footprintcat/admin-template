package com.example.backend.controller.manage.v1.system;

import com.example.backend.common.annotations.HandleControllerGlobalException;
import com.example.backend.common.annotations.PublicAccess;
import com.example.backend.common.baseobject.response.CommonReturn;
import com.example.backend.common.config.ProjectConfig;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 系统配置控制器
 *
 * @author AI Assistant
 */
@Slf4j
@HandleControllerGlobalException
@RestController
@RequestMapping("/manage/v1/system/config")
@Tag(name = "[system] 系统配置 config", description = "/manage/v1/system/config")
public class SystemConfigController {

    @Resource
    private ProjectConfig projectConfig;

    /**
     * 获取系统配置
     *
     * @return 系统配置
     */
    @PublicAccess
    @Operation(summary = "获取系统配置", description = "获取系统前端需要使用的配置信息")
    @GetMapping("/info")
    public CommonReturn getSystemConfig() {
        SystemConfigResponse response = new SystemConfigResponse();
        response.setLoginCaptchaEnabled(projectConfig.getLoginCaptchaEnabled());
        return CommonReturn.success(response);
    }

    @Data
    public static class SystemConfigResponse {
        /**
         * 是否启用登录验证码
         */
        private Boolean loginCaptchaEnabled;
    }
}
