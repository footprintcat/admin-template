package com.example.backend.modules.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.backend.modules.system.model.dto.UserDto;
import com.example.backend.modules.system.model.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 系统用户表 Mapper 接口
 * </p>
 *
 * @author coder-xiaomo
 * @since 2023-05-22
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

    User selectByUsername(String username);

    /**
     * 不修改密码
     *
     * @param row
     * @return
     */
    int updateUserInfoByPrimaryKey(User row);

    Boolean alterPassword(Long userId, @Param("newPasswordHash") String newPasswordHash);

    Page<User> getUserPage(Page<?> page, @Param("query") UserDto userDTO);

    List<User> getUserList(@Param("query") UserDto userDTO);
}
