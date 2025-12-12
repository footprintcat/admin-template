package com.example.backend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.Version;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

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
@Schema(name = "SystemUser", description = "")
@TableName("system_user")
public class SystemUser implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("username")
    private String username;

    @TableField("nickname")
    private String nickname;

    // TODO
    @TableField("role_id")
    private Integer roleId;

    // TODO
    @Schema(description = "电话")
    @TableField("telephone")
    private String telephone;

    @TableField("status")
    private String status;

    @TableLogic
    @TableField("is_delete")
    private Boolean isDelete;

    @Version
    @TableField("version")
    private Long version;

}
