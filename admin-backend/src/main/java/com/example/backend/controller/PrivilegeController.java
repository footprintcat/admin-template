package com.example.backend.controller;

import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.backend.common.Enums.PrivilegeTypeEnum;
import com.example.backend.common.Error.BusinessErrorCode;
import com.example.backend.common.Error.BusinessException;
import com.example.backend.common.Response.CommonReturnType;
import com.example.backend.common.Utils.SessionUtils;
import com.example.backend.controller.base.BaseController;
import com.example.backend.dto.PrivilegeDTO;
import com.example.backend.entity.Privilege;
import com.example.backend.entity.SystemMenu;
import com.example.backend.repository.PrivilegeRepository;
import com.example.backend.service.System.PrivilegeService;
import com.example.backend.service.System.SystemRoleService;
import com.example.backend.service.System.SystemMenuService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.CrossOrigin;
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

@CrossOrigin
@RestController
@RequestMapping("/v1/privilege")
public class PrivilegeController extends BaseController {

    @Resource
    private PrivilegeService privilegeService;
    @Resource
    private PrivilegeRepository privilegeRepository;
    @Resource
    private SystemRoleService systemRoleService;
    @Resource
    private SystemMenuService systemMenuService;

    @PostMapping("/togglePrivilege")
    public CommonReturnType add(@RequestBody JSONObject param, HttpServletRequest httpServletRequest) throws BusinessException {
        String menuId = param.getString("menuId");
        Long roleId = param.getLong("roleId");
        Boolean value = param.getBoolean("value");
        if (menuId == null || roleId == null || value == null) {
            throw new BusinessException(BusinessErrorCode.PARAMETER_VALIDATION_ERROR);
        }

        boolean result = true;
        String message = "更新成功";

        Long currentRoleId = SessionUtils.getRoleId(httpServletRequest.getSession());
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
        return CommonReturnType.success(jsonObject);
    }

    @PostMapping("/togglePrivilegeType")
    public CommonReturnType togglePrivilegeType(@RequestBody JSONObject param, HttpServletRequest httpServletRequest) throws BusinessException {
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
        Long currentRoleId = SessionUtils.getRoleId(session);
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
        return CommonReturnType.success(jsonObject);
    }

    @GetMapping("/getCurrentUserPrivilegeList")
    public CommonReturnType getCurrentUserPrivilegeList(HttpServletRequest httpServletRequest) {
        HttpSession session = httpServletRequest.getSession();
        Long userId = SessionUtils.getUserId(session);
        Long roleId = SessionUtils.getRoleId(session);

        Collection<String> currentUserPrivilegeList;
        if (Objects.equals(roleId, 1L)) {
            //  roleId = 1 的超级用户赋予全部菜单权限
            List<SystemMenu> systemMenuList = systemMenuService.getSystemMenuListWithoutRootLevel();
            currentUserPrivilegeList = systemMenuList.stream().map(SystemMenu::getMenuId).toList();
        } else {
            // 查询level为0的菜单id
            List<SystemMenu> zeroLevelMenuList = systemMenuService.getZeroLevelMenuList();
            List<String> zeroLevelMenuIdList = zeroLevelMenuList.stream().map(SystemMenu::getMenuId).toList();

            // 当前角色有权访问的菜单
            List<Privilege> rolePrivilegeList = privilegeService.getGrantedPrivilegeListByRoleId(roleId);
            // 当前用户
            List<Privilege> userPrivilegeList = privilegeService.getListByUserId(userId);
            Collection<String> currentUserGrantedMenuIdList = privilegeService.getCurrentUserPrivilegeList(rolePrivilegeList, userPrivilegeList);

            currentUserPrivilegeList = new ArrayList<>();
            currentUserPrivilegeList.addAll(zeroLevelMenuIdList);
            currentUserPrivilegeList.addAll(currentUserGrantedMenuIdList);
        }

        return CommonReturnType.success(currentUserPrivilegeList);
    }


    @GetMapping("/getUserPrivilege")
    public CommonReturnType getUserPrivilege(@RequestParam(value = "roleId") Long roleId, @RequestParam(value = "userId") Long userId) throws BusinessException {
        if (roleId == null || userId == null) {
            throw new BusinessException(BusinessErrorCode.PARAMETER_VALIDATION_ERROR);
        }
        HashMap<String, Object> result = privilegeService.getUserPrivilege(roleId, userId);
        return CommonReturnType.success(result);
    }

    @PostMapping("/saveOrUpdateUserPrivilege")
    public CommonReturnType saveOrUpdateUserPrivilege(@RequestBody PrivilegeDTO privilegeDTO) throws BusinessException {
        if (privilegeDTO.getModule() == null || privilegeDTO.getUserId() == null) {
            throw new BusinessException(BusinessErrorCode.PARAMETER_VALIDATION_ERROR);
        }
        Privilege privilege = PrivilegeDTO.toEntity(privilegeDTO);
        privilege.setUpdateTime(new Date()); // 设置权限更新时间
        Privilege oldPrivilege = privilegeService.getPrivilegeByModuleAndUserId(privilege.getModule(), privilege.getUserId());

        if (Objects.isNull(oldPrivilege)) {
            privilegeRepository.save(privilege);
        } else {
            if ("default".equals(privilege.getType())) {
                // 默认跟随角色 直接移除相应用户权限
                privilegeService.removeByModuleAndUserId(privilege);
            } else {
                privilegeService.updateByModuleAndUserId(privilege);
            }
        }
        return CommonReturnType.success();
    }

    @PostMapping("/removePrivilegesByUserId")
    public CommonReturnType removePrivilegesByUserId(@RequestBody PrivilegeDTO privilegeDTO) throws BusinessException {
        if (Objects.isNull(privilegeDTO) || privilegeDTO.getUserId() == null) {
            throw new BusinessException(BusinessErrorCode.PARAMETER_VALIDATION_ERROR);
        }
        Privilege privilege = PrivilegeDTO.toEntity(privilegeDTO);
        privilegeService.removePrivilegesByUserId(privilege.getUserId());
        return CommonReturnType.success();
    }

    /**
     * 导出权限
     *
     * @return
     */
    @GetMapping("/exportJson")
    public CommonReturnType exportJson(HttpServletRequest request) {
        Long roleId = SessionUtils.getRoleId(request.getSession());
        if (roleId != 1) {
            return CommonReturnType.error("仅超级管理用户可导出权限表");
        }
        String jsonStr = privilegeService.exportJson();
        return CommonReturnType.success(jsonStr);
    }

}
