package com.example.backend.controller.manage.v1.system;

import com.example.backend.common.annotations.HandleControllerGlobalException;
import com.example.backend.common.baseobject.response.CommonReturn;
import com.example.backend.common.error.BusinessException;
import com.example.backend.common.utils.SessionUtils;
import com.example.backend.modules.system.model.dto.MenuDto;
import com.example.backend.modules.system.service.MenuService;
import com.example.backend.modules.system.service.PrivilegeService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;

@Slf4j
@HandleControllerGlobalException
@RestController
@RequestMapping("/manage/v1/system/privilege")
@Tag(name = "[system] 权限 privilege", description = "/manage/v1/system/privilege")
public class PrivilegeController {

    @Resource
    private PrivilegeService privilegeService;
    @Resource
    private MenuService menuService;

}
