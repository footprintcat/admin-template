package com.example.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.backend.dto.UserDTO;
import com.example.backend.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * Mapper 接口
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

    Boolean alterPassword(Long userId, String newPSW);

    Page<User> getUserPageWithoutSuAccount(Page<?> page, @Param("query") UserDTO userDTO);

    List<User> getUserListWithoutSuAccount(@Param("query") UserDTO userDTO);
}
