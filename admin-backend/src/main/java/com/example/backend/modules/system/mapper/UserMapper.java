package com.example.backend.modules.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.backend.common.baseobject.db.OrderByItem;
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

    /**
     * 获取用户分页列表（支持排序）
     *
     * @param page        分页对象
     * @param userDTO     查询条件
     * @param orderByItem 排序参数列表
     * @return
     */
    Page<User> getUserPage(Page<?> page, @Param("query") UserDto userDTO, @Param("orderByItem") List<OrderByItem> orderByItem);

    List<User> getUserList(@Param("query") UserDto userDTO);
}
