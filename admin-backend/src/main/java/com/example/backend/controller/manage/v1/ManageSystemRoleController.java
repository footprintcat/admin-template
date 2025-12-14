package com.example.backend.controller.manage.v1;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.backend.common.error.BusinessErrorCode;
import com.example.backend.common.error.BusinessException;
import com.example.backend.common.PageTable.builder.FieldBuilder;
import com.example.backend.common.PageTable.builder.FieldMapperBuilder;
import com.example.backend.common.PageTable.builder.FieldRuleListBuilder;
import com.example.backend.common.PageTable.enums.AddType;
import com.example.backend.common.PageTable.enums.EditType;
import com.example.backend.common.PageTable.enums.FieldType;
import com.example.backend.common.PageTable.enums.SearchType;
import com.example.backend.common.baseobject.response.CommonReturn;
import com.example.backend.common.baseobject.controller.BaseController;
import com.example.backend.modules.system.model.dto.SystemRoleDto;
import com.example.backend.modules.system.model.entity.SystemRole;
import com.example.backend.common.baseobject.request.PageQuery;
import com.example.backend.modules.system.service.needrefactor.SystemRoleServiceV2;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/v2/manage/system/role")
public class ManageSystemRoleController extends BaseController {

    @Resource
    private SystemRoleServiceV2 systemRoleServiceV2;

    /**
     * 获取角色列表
     *
     * @param pageQuery     分页参数
     * @param systemRoleDTO 筛选条件
     * @return
     */
    @GetMapping("/list")
    @ResponseBody
    public CommonReturn list(PageQuery pageQuery, SystemRoleDto systemRoleDTO) {
        // 查询分页数据
        Page<SystemRole> rolePage = systemRoleServiceV2.getRolePage(pageQuery, systemRoleDTO);

        // 分页数据转为 DTO
        List<SystemRole> systemRoleList = rolePage.getRecords();
        List<SystemRoleDto> systemRoleDtoList = SystemRoleDto.fromEntity(systemRoleList);

        // id列 字段名（区分大小写；以VO中的变量名为准）
        // 新增、修改弹窗时，使用该列作为主键列进行操作
        String idFieldName = "id";

        // 当前管理页面
        String pageName = "角色列表";

        // 指定前端表格显示列
        JSONArray columns = FieldBuilder.create()
                .add("roleName", "roleName", "角色名称", "",
                        FieldType.TEXT, SearchType.CAN_NOT_SEARCH, AddType.CAN_NOT_ADD, EditType.CAN_NOT_EDIT,
                        FieldBuilder.SEARCH_PLACEHOLDER_SAME_AS_FIELDNAME,
                        "角色名称", FieldBuilder.EDIT_PLACEHOLDER_SAME_AS_ADD_PLACEHOLDER,
                        FieldRuleListBuilder.create(),
                        null
                )
                // .add("comment", "comment", "备注", "",
                //         FieldType.TEXT, SearchType.CAN_NOT_SEARCH, AddType.CAN_NOT_ADD, EditType.CAN_NOT_EDIT,
                //         FieldBuilder.SEARCH_PLACEHOLDER_SAME_AS_FIELDNAME,
                //         "备注", FieldBuilder.EDIT_PLACEHOLDER_SAME_AS_ADD_PLACEHOLDER,
                //         FieldRuleListBuilder.create(),
                //         null
                // )
                .build();

        // 指定需要翻译的字段
        JSONArray fieldMapper = FieldMapperBuilder.create()
                // .add("monitorCode", "monitorCode", monitorCodeSelectMap)
                .build();

        // 拼装返回结果
        JSONObject map = new JSONObject();
        map.put("total", rolePage.getTotal());
        map.put("list", systemRoleDtoList);
        map.put("columns", columns);
        map.put("fieldMapper", fieldMapper);
        map.put("idFieldName", idFieldName);
        map.put("pageName", pageName);

        // 返回结果
        return CommonReturn.success(map);
    }

    /**
     * 新增 / 编辑
     *
     * @return
     */
    @PostMapping("/edit")
    @ResponseBody
    public CommonReturn edit(@ModelAttribute SystemRoleDto systemRoleDTO) {

        // 传入参数 - 要修改的设备
        SystemRole systemRole = SystemRoleDto.toEntity(systemRoleDTO);

        // 通过 staffId 查询系统中是否存在该设备
        SystemRole existSystemRole = systemRoleServiceV2.getRoleById(systemRole.getId());

        if (systemRole.getId() == null || systemRole.getId() < 1) {
            // 新增
            if (existSystemRole != null) {
                return CommonReturn.error("角色已存在，操作失败");
            }
            systemRole.setId(null);
            systemRoleServiceV2.addRole(systemRole);
        } else {
            // 修改
            if (existSystemRole == null) {
                return CommonReturn.error("角色不存在，操作失败");
            }
            systemRoleServiceV2.updateRole(systemRole);
        }
        return CommonReturn.success();
    }

    /**
     * 删除
     *
     * @param id
     * @return
     */
    @PostMapping("/delete")
    @ResponseBody
    public CommonReturn delete(Integer id) throws BusinessException {
        throw new BusinessException(BusinessErrorCode.NOT_SUPPORT, "不允许删除报警记录");
    }

    /**
     * 导出列表
     *
     * @return
     */
    @GetMapping("/export")
    @ResponseBody
    public CommonReturn exportRoleList(SystemRoleDto systemRoleDTO) {
        List<SystemRole> systemRoleList = systemRoleServiceV2.getRoleList(systemRoleDTO);
        List<SystemRoleDto> systemRoleDtoList = SystemRoleDto.fromEntity(systemRoleList);

        // 当前时间
        Date now = Calendar.getInstance().getTime();
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String dateTime = format.format(now);

        HashMap<String, Object> map = new HashMap<>();
        map.put("list", systemRoleDtoList);
        map.put("sheetName", "角色表-" + System.currentTimeMillis());
        map.put("fileName", "角色表_导出时间_" + dateTime);

        return CommonReturn.success(map);
    }

}
