package com.example.backend.modules.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 *
 * </p>
 *
 * @author coder-xiaomo
 * @since 2025-03-04
 */
@Getter
@Setter
@Schema(name = "Privilege", description = "")
@TableName("privilege")
@Deprecated
public class Privilege implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "主键id")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @Schema(description = "角色id")
    @TableField("role_id")
    private Long roleId;

    @Schema(description = "用户id")
    @TableField("user_id")
    private Long userId;

    @Schema(description = "模块名称")
    @TableField("module")
    private String module;

    @Schema(description = "权限类型(	有权：granted；	无权：denied；	有权继承：inheritable；	)")
    @TableField("type")
    private String type;

    @Schema(description = "过期时间")
    @TableField("expire_time")
    private Date expireTime;

    @Schema(description = "最后更新时间")
    @TableField("update_time")
    private Date updateTime;
}
