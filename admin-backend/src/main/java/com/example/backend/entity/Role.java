package com.example.backend.entity;

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
 * @since 2023-05-22
 */
@Getter
@Setter
@Schema(name = "Role", description = "")
@TableName("role")
public class Role implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField("role_name")
    private String roleName;

    @TableField("comment")
    private String comment;

    @TableField("parent_role_id")
    private Integer parentRoleId;

    @Schema(description = "最后更新时间")
    @TableField("update_time")
    private Date updateTime;
}
