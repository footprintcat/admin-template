package com.example.backend.modules.system.service.needrefactor;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.backend.common.baseobject.db.OrderByItem;
import com.example.backend.common.baseobject.request.PageQuery;
import com.example.backend.common.baseobject.request.SortColumnRequestItem;
import com.example.backend.common.error.BusinessErrorCode;
import com.example.backend.common.error.BusinessException;
import com.example.backend.common.utils.SessionUtils;
import com.example.backend.modules.system.mapper.UserMapper;
import com.example.backend.modules.system.model.dto.UserDto;
import com.example.backend.modules.system.model.entity.User;
import jakarta.annotation.Nullable;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Service
public class SystemUserServiceV2 {

    @Resource
    private UserMapper userMapper;

    /**
     * 获取当前登录用户
     *
     * @return User
     */
    public User getCurrentLoginUser(HttpSession session) {
        try {
            Long userId = SessionUtils.getUserId(session);
            if (userId == null) {
                return null;
            }
            return userMapper.selectById(userId);
        } catch (Exception e) {
            return null;
        }
    }

    public User getCurrentLoginUser(HttpServletRequest httpServletRequest) {
        return getCurrentLoginUser(httpServletRequest.getSession());
    }

    /**
     * 通过用户名获取用户
     *
     * @param username
     * @return
     */
    public User getUserByUsername(String username) {
        if (username == null) {
            return null;
        }
        return userMapper.selectByUsername(username);
    }

    /**
     * 通过 userId 获取用户
     *
     * @param userId
     * @return
     */
    public User getUserById(Long userId) {
        if (userId == null || userId <= 0) {
            return null;
        }
        return userMapper.selectById(userId);
    }

    /**
     * 新增用户
     *
     * @param user
     * @return
     */
    public void addUser(User user) {
        if (user == null) {
            return;
        }
        userMapper.insert(user);
    }

    /**
     * 修改用户信息
     *
     * @param user
     * @return
     */
    public void updateUser(User user) {
        if (user == null) {
            return;
        }
        userMapper.updateUserInfoByPrimaryKey(user);
    }

    /**
     * 删除用户
     * 删除前会判断是否有权限对该用户进行操作
     * <p>
     *  TODO 超级管理员与管理员权限进行区分
     *
     * @param session
     * @param inputUserId
     * @throws BusinessException 业务异常
     */
    public void deleteUserWithVerify(HttpSession session, Long inputUserId) throws BusinessException {
        // 未登录状态
        if (!SessionUtils.isLogin(session)) {
            throw new BusinessException(BusinessErrorCode.USER_NOT_LOGIN);
        }

        // TODO
        // 该用户角色没有权限删除用户
        // TODO
        Long loginUserRoleId = null; // SessionUtils.getRoleId(session);
        if (loginUserRoleId != 1 && loginUserRoleId != 2) {
            throw new BusinessException(BusinessErrorCode.OPERATION_NOT_ALLOWED);
        }

        User userToDelete = userMapper.selectById(inputUserId);
        if (userToDelete == null) {
            throw new BusinessException(BusinessErrorCode.USER_NOT_EXIST, "要删除的用户不存在");
        }

        // 是自己 不能删
        Long loginUserId = SessionUtils.getUserId(session);
        Long userToDeleteId = userToDelete.getId();
        if (Objects.equals(loginUserId, userToDeleteId)) {
            throw new BusinessException(BusinessErrorCode.OPERATION_NOT_ALLOWED, "不能删除自己");
        }

        // 要删除用户是管理员身份 不允许删除
        // TODO
        // Long userToDeleteRoleId = userToDelete.getRoleId();
        // if (userToDeleteRoleId == 1) {
        //     throw new BusinessException(BusinessErrorCode.OPERATION_NOT_ALLOWED, "不允许删除超级用户");
        // } else if (!systemRoleService.canEmpowerTargetRole(loginUserRoleId, userToDeleteRoleId)) {
        //     throw new BusinessException(BusinessErrorCode.OPERATION_NOT_ALLOWED, "无权删除该用户");
        // }

        // 执行删除
        userMapper.deleteById(userToDeleteId);
    }

    /**
     * 获取用户分页列表
     *
     * @param pageQuery
     * @param userDTO
     * @param sortList  排序参数列表
     * @return
     */
    public Page<User> getUserPage(@NotNull PageQuery pageQuery, @NotNull UserDto userDTO, @NotNull List<SortColumnRequestItem> sortList) throws BusinessException {
        Page<User> page = new Page<>(pageQuery.getPageIndex(), pageQuery.getPageSize());
        return userMapper.getUserPage(page, userDTO, _validateSortList(sortList));
    }

    /**
     * 列表查询允许排序的字段
     */
    private static final Map<String, String> FIELD_TO_DB_COLUMN_MAP = new HashMap<>();

    static {
        FIELD_TO_DB_COLUMN_MAP.put("id", "id");
        FIELD_TO_DB_COLUMN_MAP.put("username", "username");
        FIELD_TO_DB_COLUMN_MAP.put("nickname", "nickname");
        FIELD_TO_DB_COLUMN_MAP.put("status", "status");
    }

    /**
     * 验证并转换排序参数（将前端字段名转换为数据库列名）
     *
     * @param sortList 原始排序参数列表
     * @return 验证后的排序参数列表
     */
    private List<OrderByItem> _validateSortList(@NotNull List<SortColumnRequestItem> sortList) throws BusinessException {
        @NotNull List<OrderByItem> orderByItemList = new ArrayList<>();
        for (SortColumnRequestItem requestItem : sortList) {
            String field = requestItem.getField();
            boolean descSort = requestItem.isDescSort();

            @Nullable String dbColumn = FIELD_TO_DB_COLUMN_MAP.get(field);
            if (dbColumn == null) {
                throw new BusinessException(BusinessErrorCode.PARAMETER_VALIDATION_ERROR, "未知的排序字段: " + field);
            }

            OrderByItem orderByItem = new OrderByItem();
            orderByItemList.add(orderByItem.setField(dbColumn).setIsDesc(descSort));
        }
        return orderByItemList;
    }

    /**
     * 导出全部用户
     *
     * @return
     */
    public List<User> getUserList(@NotNull UserDto userDTO) {
        return userMapper.getUserList(userDTO);
    }
}
