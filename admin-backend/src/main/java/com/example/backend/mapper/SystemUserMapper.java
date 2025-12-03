package com.example.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.backend.dto.SystemUserDTO;
import com.example.backend.entity.SystemUser;
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
public interface SystemUserMapper extends BaseMapper<SystemUser> {

    SystemUser selectByUsername(String username);

    /**
     * 不修改密码
     *
     * @param row
     * @return
     */
    int updateUserInfoByPrimaryKey(SystemUser row);

    Boolean alterPassword(Long userId, @Param("newPasswordHash") String newPasswordHash);

    Page<SystemUser> getUserPageWithoutSuAccount(Page<?> page, @Param("query") SystemUserDTO systemUserDTO);

    List<SystemUser> getUserListWithoutSuAccount(@Param("query") SystemUserDTO systemUserDTO);
}
