package com.example.backend.modules.system.model.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
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
import java.time.LocalDateTime;

/**
 * <p>
 * 系统职位信息表
 * </p>
 *
 * @author coder-xiaomo
 * @since 2025-12-14
 */
@Getter
@Setter
@Schema(name = "JobPosition", description = "系统职位信息表")
@TableName("system_job_position")
public class JobPosition implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "雪花id")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @Schema(description = "职位编号，如HR-001")
    @TableField("position_code")
    private String positionCode;

    @Schema(description = "职位名称")
    @TableField("position_name")
    private String positionName;

    @Schema(description = "所属部门ID")
    @TableField("department_id")
    private Long departmentId;

    @Schema(description = "职位类别：TECH-技术类, MARKET-市场类, SALES-销售类, HR-人力类, FINANCE-财务类, ADMIN-行政类")
    @TableField("position_category")
    private String positionCategory;

    @Schema(description = "职位层级：INTERN-实习, JUNIOR-初级, MIDDLE-中级, SENIOR-高级, LEADER-主管, MANAGER-经理, DIRECTOR-总监, VP-副总裁, PRESIDENT-总裁")
    @TableField("position_level")
    private String positionLevel;

    @Schema(description = "直接上级职位ID")
    @TableField("parent_id")
    private Long parentId;

    @Schema(description = "状态：ACTIVE-启用, INACTIVE-停用, PLANNING-编制中")
    @TableField("status")
    private String status;

    @Schema(description = "工作地点")
    @TableField("work_location")
    private String workLocation;

    @Schema(description = "排序号")
    @TableField("sort_order")
    private Integer sortOrder;

    @Schema(description = "职位简介")
    @TableField("description")
    private String description;

    @Schema(description = "备注")
    @TableField("comment")
    private String comment;

    @Schema(description = "租户id")
    @TableField("tenant_id")
    private Long tenantId;

    @Schema(description = "创建人")
    @TableField("create_by")
    private Long createBy;

    @Schema(description = "更新人")
    @TableField("update_by")
    private Long updateBy;

    @Schema(description = "创建时间")
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @Schema(description = "最后更新时间")
    @TableField("update_time")
    private LocalDateTime updateTime;

    @Schema(description = "逻辑删除")
    @TableField("delete_time")
    @TableLogic
    private LocalDateTime deleteTime;

    @Schema(description = "版本号（乐观锁）")
    @TableField("version")
    @Version
    private Long version;
}
