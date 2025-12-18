/*
package com.example.backend.controller.manage.v1;

import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.backend.common.annotations.HandleControllerGlobalException;
import com.example.backend.modules.system.enums.PrivilegeTypeEnum;
import com.example.backend.common.error.BusinessErrorCode;
import com.example.backend.common.error.BusinessException;
import com.example.backend.common.baseobject.response.CommonReturn;
import com.example.backend.common.utils.SessionUtils;
import com.example.backend.common.baseobject.controller.BaseController;
import com.example.backend.modules.system.model.dto.SystemPrivilegeDto;
import com.example.backend.modules.system.model.entity.Privilege;
import com.example.backend.modules.system.model.entity.SystemMenu;
import com.example.backend.modules.system.service.needrefactor.SystemRoleService;
import com.example.backend.modules.system.service.needrefactor.SystemMenuService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

@Slf4j
@HandleControllerGlobalException
@RestController
@RequestMapping("/v1/privilege")
public class SystemPrivilegeController {

    @Resource
    private SystemRoleService systemRoleService;
    @Resource
    private SystemMenuService systemMenuService;

    @PostMapping("/togglePrivilege")
    public CommonReturn add(@RequestBody JSONObject param, HttpServletRequest httpServletRequest) throws BusinessException {
        String menuId = param.getString("menuId");
        Long roleId = param.getLong("roleId");
        Boolean value = param.getBoolean("value");
        if (menuId == null || roleId == null || value == null) {
            throw new BusinessException(BusinessErrorCode.PARAMETER_VALIDATION_ERROR);
        }

        boolean result = true;
        String message = "更新成功";

        // TODO
        Long currentRoleId = null; // SessionUtils.getRoleId(httpServletRequest.getSession());
        if (Objects.equals(currentRoleId, roleId)) {
            // throw new BusinessException(BusinessErrorCode.OPERATION_NOT_ALLOWED, "不可修改当前用户的权限");
            result = false;
            message = "不可修改当前用户的权限";
        } else {

            // TODO 验证 roleId 是否正确
            // TODO 验证 menuId 是否正确

            // TODO 验证当前用户是否有操作权限

            // 查询旧的记录
            LambdaQueryWrapper<Privilege> oldPrivilegeQueryWrapper = new LambdaQueryWrapper<Privilege>()
                    .eq(Privilege::getRoleId, roleId)
                    .eq(Privilege::getModule, menuId)
                    .last("LIMIT 1");
            Privilege oldPrivilege = privilegeRepository.getOne(oldPrivilegeQueryWrapper);

            if (value) {
                // 为其新添加权限
                if (oldPrivilege == null) {
                    Privilege privilege = new Privilege();
                    privilege.setModule(menuId);
                    privilege.setRoleId(roleId);
                    privilegeRepository.save(privilege);
                } else {
                    result = false;
                    message = "状态错误，请刷新重试";
                }
            } else {
                // 撤销其权限
                if (oldPrivilege == null) {
                    result = false;
                    message = "状态错误，请刷新重试";
                } else {
                    privilegeRepository.remove(oldPrivilegeQueryWrapper);
                }
            }
        }

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("result", result);
        jsonObject.put("message", message);
        return CommonReturn.success(jsonObject);
    }

    @PostMapping("/togglePrivilegeType")
    public CommonReturn togglePrivilegeType(@RequestBody JSONObject param, HttpServletRequest httpServletRequest) throws BusinessException {
        String menuId = param.getString("menuId");
        Long roleId = param.getLong("roleId");
        Boolean value = param.getBoolean("value");
        String type = param.getString("type"); // 权限类型
        if (menuId == null || roleId == null || value == null || type == null) {
            throw new BusinessException(BusinessErrorCode.PARAMETER_VALIDATION_ERROR);
        }
        boolean result = true;
        String message = "更新成功";

        HttpSession session = httpServletRequest.getSession();
        // TODO
        Long currentRoleId = null; // SessionUtils.getRoleId(session);
        if (Objects.equals(currentRoleId, roleId)) {
            // throw new BusinessException(BusinessErrorCode.OPERATION_NOT_ALLOWED, "不可修改当前用户的权限");
            result = false;
            message = "不可修改当前用户的权限";
        } else {
            // 查询当前用户对此菜单项的权限
            LambdaQueryWrapper<Privilege> privilegeQueryWrapper = new LambdaQueryWrapper<Privilege>()
                    .eq(Privilege::getRoleId, currentRoleId)
                    .eq(Privilege::getModule, menuId)
                    .last("LIMIT 1");
            Privilege currentUserPrivilege = privilegeRepository.getOne(privilegeQueryWrapper);

            // 判断被赋权用户是否是当前用户的子用户
            boolean canEmpowerTargetRole = systemRoleService.canEmpowerTargetRole(currentRoleId, roleId);
            if (Objects.equals(currentRoleId, 1L) || (Objects.nonNull(currentUserPrivilege) && PrivilegeTypeEnum.INHERITABLE.getCode().equals(currentUserPrivilege.getType()) && canEmpowerTargetRole)) {
                // 查询旧的记录
                LambdaQueryWrapper<Privilege> oldPrivilegeQueryWrapper = new LambdaQueryWrapper<Privilege>()
                        .eq(Privilege::getRoleId, roleId)
                        .eq(Privilege::getModule, menuId)
                        .last("LIMIT 1");
                Privilege oldPrivilege = privilegeRepository.getOne(oldPrivilegeQueryWrapper);
                if (Objects.nonNull(oldPrivilege)) {
                    oldPrivilege.setUpdateTime(new Date());
                }

                // 当前用户有权继承时，即可对其子用户赋权
                if (value) {
                    if (oldPrivilege == null) {
                        Privilege privilege = new Privilege();
                        privilege.setModule(menuId);
                        privilege.setRoleId(roleId);
                        privilege.setType(type);
                        privilege.setUpdateTime(new Date());
                        privilegeRepository.save(privilege);
                    }
                    // 当存在旧权限，仍出现勾选情况
                    else if (PrivilegeTypeEnum.DENIED.getCode().equals(oldPrivilege.getType())) {
                        // 无权 =》 有权
                        if (PrivilegeTypeEnum.GRANTED.getCode().equals(type)) {
                            oldPrivilege.setType(PrivilegeTypeEnum.GRANTED.getCode());
                            privilegeRepository.updateById(oldPrivilege);
                        }
                        // 无权 =》 有权继承
                        else {
                            oldPrivilege.setType(PrivilegeTypeEnum.INHERITABLE.getCode());
                            privilegeRepository.updateById(oldPrivilege);
                        }
                    } else if (PrivilegeTypeEnum.GRANTED.getCode().equals(oldPrivilege.getType())) {
                        // 有权 =》 有权继承
                        oldPrivilege.setType(PrivilegeTypeEnum.INHERITABLE.getCode());
                        privilegeRepository.updateById(oldPrivilege);
                    }
                } else {
                    if (Objects.isNull(oldPrivilege)) {
                        result = false;
                        message = "状态错误，请刷新重试";
                    } else if (PrivilegeTypeEnum.GRANTED.getCode().equals(type)) {
                        // 如果取消勾选授权，则撤销其权限
                        // privilegeRepository.remove(oldPrivilegeQueryWrapper);

                        // 如果取消勾选授权，则将权限类型更为无权
                        oldPrivilege.setType(PrivilegeTypeEnum.DENIED.getCode());
                        privilegeRepository.updateById(oldPrivilege);
                    } else {
                        // 如果取消勾选的是有权继承, 则将其权限改为有权
                        oldPrivilege.setType(PrivilegeTypeEnum.GRANTED.getCode());
                        privilegeRepository.updateById(oldPrivilege);
                    }
                }
            } else {
                // 无权继承或非子用户
                result = false;
                message = "无权修改其权限，请联系管理员";
            }
        }

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("result", result);
        jsonObject.put("message", message);
        return CommonReturn.success(jsonObject);
    }

    @GetMapping("/getCurrentUserPrivilegeList")
    public CommonReturn getCurrentUserPrivilegeList(HttpServletRequest httpServletRequest) {
        HttpSession session = httpServletRequest.getSession();
        Long userId = SessionUtils.getUserId(session);
        // TODO
        Long roleId = null; // SessionUtils.getRoleId(session);

        Collection<String> currentUserPrivilegeList;
        if (Objects.equals(roleId, 1L)) {
            //  roleId = 1 的超级用户赋予全部菜单权限
            List<SystemMenu> systemMenuList = systemMenuService.getSystemMenuListWithoutRootLevel();
            currentUserPrivilegeList = systemMenuList.stream().map(SystemMenu::getMenuCode).toList();
        } else {
            // 查询level为0的菜单id
            List<SystemMenu> zeroLevelMenuList = systemMenuService.getZeroLevelMenuList();
            List<String> zeroLevelMenuIdList = zeroLevelMenuList.stream().map(SystemMenu::getMenuCode).toList();

            // 当前角色有权访问的菜单
            List<Privilege> rolePrivilegeList = systemPrivilegeService.getGrantedPrivilegeListByRoleId(roleId);
            // 当前用户
            List<Privilege> userPrivilegeList = systemPrivilegeService.getListByUserId(userId);
            Collection<String> currentUserGrantedMenuIdList = systemPrivilegeService.getCurrentUserPrivilegeList(rolePrivilegeList, userPrivilegeList);

            currentUserPrivilegeList = new ArrayList<>();
            currentUserPrivilegeList.addAll(zeroLevelMenuIdList);
            currentUserPrivilegeList.addAll(currentUserGrantedMenuIdList);
        }

        return CommonReturn.success(currentUserPrivilegeList);
    }


    @GetMapping("/getUserPrivilege")
    public CommonReturn getUserPrivilege(@RequestParam(value = "roleId") Long roleId, @RequestParam(value = "userId") Long userId) throws BusinessException {
        if (roleId == null || userId == null) {
            throw new BusinessException(BusinessErrorCode.PARAMETER_VALIDATION_ERROR);
        }
        HashMap<String, Object> result = systemPrivilegeService.getUserPrivilege(roleId, userId);
        return CommonReturn.success(result);
    }

    @PostMapping("/saveOrUpdateUserPrivilege")
    public CommonReturn saveOrUpdateUserPrivilege(@RequestBody SystemPrivilegeDto systemPrivilegeDTO) throws BusinessException {
        if (systemPrivilegeDTO.getModule() == null || systemPrivilegeDTO.getUserId() == null) {
            throw new BusinessException(BusinessErrorCode.PARAMETER_VALIDATION_ERROR);
        }
        Privilege privilege = SystemPrivilegeDto.toEntity(systemPrivilegeDTO);
        privilege.setUpdateTime(new Date()); // 设置权限更新时间
        Privilege oldPrivilege = systemPrivilegeService.getPrivilegeByModuleAndUserId(privilege.getModule(), privilege.getUserId());

        if (Objects.isNull(oldPrivilege)) {
            privilegeRepository.save(privilege);
        } else {
            if ("default".equals(privilege.getType())) {
                // 默认跟随角色 直接移除相应用户权限
                systemPrivilegeService.removeByModuleAndUserId(privilege);
            } else {
                systemPrivilegeService.updateByModuleAndUserId(privilege);
            }
        }
        return CommonReturn.success();
    }

    @PostMapping("/removePrivilegesByUserId")
    public CommonReturn removePrivilegesByUserId(@RequestBody SystemPrivilegeDto systemPrivilegeDTO) throws BusinessException {
        if (Objects.isNull(systemPrivilegeDTO) || systemPrivilegeDTO.getUserId() == null) {
            throw new BusinessException(BusinessErrorCode.PARAMETER_VALIDATION_ERROR);
        }
        Privilege privilege = SystemPrivilegeDto.toEntity(systemPrivilegeDTO);
        systemPrivilegeService.removePrivilegesByUserId(privilege.getUserId());
        return CommonReturn.success();
    }

    */
/**
     * 导出权限
     *
     * @return
     *//*

    @GetMapping("/exportJson")
    public CommonReturn exportJson(HttpServletRequest request) {
        // TODO
        Long roleId = null; // SessionUtils.getRoleId(request.getSession());
        if (roleId != 1) {
            return CommonReturn.error("仅超级管理用户可导出权限表");
        }
        String jsonStr = systemPrivilegeService.exportJson();
        return CommonReturn.success(jsonStr);
    }

}
*/
