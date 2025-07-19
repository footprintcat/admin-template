package com.example.backend.service.v2;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.backend.common.Error.BusinessErrorCode;
import com.example.backend.common.Error.BusinessException;
import com.example.backend.common.Utils.SessionUtils;
import com.example.backend.dto.UserDTO;
import com.example.backend.entity.User;
import com.example.backend.mapper.UserMapper;
import com.example.backend.query.PageQuery;
import com.example.backend.service.System.RoleService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.apache.commons.codec.digest.DigestUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class UserServiceV2 {

    @Resource
    private UserMapper userMapper;
    @Resource
    private RoleService roleService;

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
     * 判断用户密码是否正确，并且登录
     *
     * @param user
     * @param password
     * @return
     */
    public boolean checkPasswordIsCorrect(User user, String password) {
        if (user == null) {
            return false;
        }
        String inputEncryptPwd = DigestUtils.sha512Hex(password);
        String userEncryptPwd = user.getPassword();
        return Objects.equals(inputEncryptPwd, userEncryptPwd);
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
     * @throws BusinessException
     */
    public void deleteUserWithVerify(HttpSession session, Long inputUserId) throws BusinessException {
        // 未登录状态
        if (!SessionUtils.isLogin(session)) {
            throw new BusinessException(BusinessErrorCode.USER_NOT_LOGIN);
        }

        // 该用户角色没有权限删除用户
        Integer loginUserRoleId = SessionUtils.getRoleId(session);
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
        Integer userToDeleteRoleId = userToDelete.getRoleId();
        if (userToDeleteRoleId == 1) {
            throw new BusinessException(BusinessErrorCode.OPERATION_NOT_ALLOWED, "不允许删除超级用户");
        } else if (!roleService.canEmpowerTargetRole(loginUserRoleId, userToDeleteRoleId)) {
            throw new BusinessException(BusinessErrorCode.OPERATION_NOT_ALLOWED, "无权删除该用户");
        }

        // 执行删除
        userMapper.deleteById(userToDeleteId);
    }

    /**
     * 获取用户分页列表
     *
     * @param pageQuery
     * @return
     */
    public Page<User> getUserPageWithoutSuAccount(PageQuery pageQuery, @NotNull UserDTO userDTO) {
        Page<User> page = new Page<>(pageQuery.getPageIndex(), pageQuery.getPageSize());
        return userMapper.getUserPageWithoutSuAccount(page, userDTO);
    }

    /**
     * 导出全部用户
     *
     * @return
     */
    public List<User> getUserListWithoutSuAccount(@NotNull UserDTO userDTO) {
        return userMapper.getUserListWithoutSuAccount(userDTO);
    }
}
