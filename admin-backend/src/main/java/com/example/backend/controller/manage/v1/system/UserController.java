package com.example.backend.controller.manage.v1.system;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.backend.common.PageTable.builder.FieldBuilder;
import com.example.backend.common.PageTable.builder.FieldMapperBuilder;
import com.example.backend.common.PageTable.builder.FieldRuleBuilder;
import com.example.backend.common.PageTable.builder.FieldRuleListBuilder;
import com.example.backend.common.PageTable.enums.AddType;
import com.example.backend.common.PageTable.enums.EditType;
import com.example.backend.common.PageTable.enums.FieldType;
import com.example.backend.common.PageTable.enums.SearchType;
import com.example.backend.common.annotations.HandleControllerGlobalException;
import com.example.backend.common.baseobject.request.PageQuery;
import com.example.backend.common.baseobject.response.CommonReturn;
import com.example.backend.common.baseobject.response.ManageListResponse;
import com.example.backend.common.error.BusinessException;
import com.example.backend.common.utils.ExportExcelUtils;
import com.example.backend.controller.manage.v1.system.dto.request.user.ManageSystemUserExportRequest;
import com.example.backend.controller.manage.v1.system.dto.request.user.ManageSystemUserListRequest;
import com.example.backend.modules.system.model.converter.UserConverter;
import com.example.backend.modules.system.model.dto.UserDto;
import com.example.backend.modules.system.model.dto.export.UserExportDto;
import com.example.backend.modules.system.model.entity.User;
import com.example.backend.modules.system.service.needrefactor.SystemRoleServiceV2;
import com.example.backend.modules.system.service.needrefactor.SystemUserServiceV2;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@HandleControllerGlobalException
@RestController
@RequestMapping("/manage/v1/system/user")
@Tag(name = "[system] 用户 user", description = "/manage/v1/system/user")
public class UserController {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Resource
    private UserConverter userConverter;
    @Resource
    private SystemUserServiceV2 systemUserServiceV2;
    @Resource
    private SystemRoleServiceV2 systemRoleServiceV2;

    /**
     * 获取用户列表
     *
     * @param requestBody 筛选条件
     * @return
     */
    @PostMapping("/list")
    public CommonReturn list(@RequestBody ManageSystemUserListRequest requestBody) throws BusinessException {

        // 查询分页数据
        Page<User> page = systemUserServiceV2.getUserPage(
                requestBody.getPageQuery(),
                requestBody.getParams(),
                requestBody.getSort()
        );

        // 分页数据转为 DTO
        List<User> list = page.getRecords();
        List<UserDto> dtoList = UserDto.fromEntity(list);

        ManageListResponse<UserDto> response = ManageListResponse.<UserDto>create()
                .setTotal(page.getTotal())
                .setList(dtoList);

        return CommonReturn.success(response);
    }

    /**
     * 导出用户列表
     *
     * @param requestBody
     * @param response
     * @return
     * @throws IOException
     */
    @PostMapping("/export")
    public void export(@RequestBody ManageSystemUserExportRequest requestBody, HttpServletResponse response) throws IOException {
        final String fileName = "用户列表_" + ExportExcelUtils.getFormattedDateInFileName() + ".xlsx";
        final String sheetName = "用户表";

        // 响应头
        ExportExcelUtils.setResponseHeader(response, fileName);

        // 查询数据
        UserDto params = requestBody.getParams();
        List<User> userList = systemUserServiceV2.getUserList(params);
        List<UserExportDto> exportDto = userConverter.toExportDto(userList);

        // 导出 xlsx
        ExportExcelUtils.exportToStream(exportDto, UserExportDto.class, sheetName, response.getOutputStream());
    }

    /**
     * 导出用户列表
     *
     * @param requestBody
     * @return
     * @throws IOException
     */
    @PostMapping("/export-data")
    public CommonReturn exportData(@RequestBody ManageSystemUserExportRequest requestBody) {
        final String fileName = "用户列表_" + ExportExcelUtils.getFormattedDateInFileName();
        final String sheetName = "用户表";

        // 查询数据
        UserDto params = requestBody.getParams();
        List<User> userList = systemUserServiceV2.getUserList(params);
        List<UserExportDto> exportDto = userConverter.toExportDto(userList);

        HashMap<String, Object> map = new HashMap<>();
        map.put("data", exportDto);
        map.put("fileName", fileName);
        map.put("sheetName", sheetName);
        return CommonReturn.success(map);
    }

    /**
     * 获取用户列表
     *
     * @param pageQuery 分页参数
     * @param userDTO   筛选条件
     * @return
     */
    @GetMapping("/list")
    @ResponseBody
    @Deprecated
    public CommonReturn list(PageQuery pageQuery, UserDto userDTO) throws BusinessException {
        // 查询分页数据
        Page<User> systemUserPage = systemUserServiceV2.getUserPage(pageQuery, userDTO, Collections.emptyList());

        // 查询 roleMap
        HashMap<Long, String> roleMap = systemRoleServiceV2.getRoleMap();

        // 用于 role 下拉框 mock 数据
        String roleListForMock = OBJECT_MAPPER.valueToTree(
                roleMap.keySet().stream().map(Object::toString).collect(Collectors.toList())
        ).toString();

        // 分页数据转为 DTO
        List<User> userList = systemUserPage.getRecords();
        List<UserDto> userDtoList = UserDto.fromEntity(userList);

        // id列 字段名（区分大小写；以VO中的变量名为准）
        // 新增、修改弹窗时，使用该列作为主键列进行操作
        String idFieldName = "id";

        // 当前管理页面
        String pageName = "用户管理";

        // 指定前端表格显示列
        ArrayNode columns = FieldBuilder.create()
                .add("username", "username", "账号", "",
                        FieldType.TEXT, SearchType.INPUT, AddType.INPUT, EditType.PLAIN_TEXT,
                        FieldBuilder.SEARCH_PLACEHOLDER_SAME_AS_FIELDNAME,
                        "用户名", FieldBuilder.EDIT_PLACEHOLDER_SAME_AS_ADD_PLACEHOLDER,
                        FieldRuleListBuilder.create()
                                .add(FieldRuleBuilder.create("用户名").required())
                                .add(FieldRuleBuilder.create("用户名").minMax(2, 10)),
                        "DPD @word(2, 10)"
                )
                .add("password", "password", "密码", "",
                        FieldType.HIDDEN, SearchType.CAN_NOT_SEARCH, AddType.INPUT, EditType.INPUT,
                        null,
                        "必填", "若不修改则留空",
                        FieldRuleListBuilder.create()
                                .add(FieldRuleBuilder.create("用户密码").minMax(4, 16)),
                        "DPD @word(4, 16)"
                )
                .add("roleId", "roleName", "角色", "",
                        FieldType.TEXT, SearchType.SELECT, AddType.SELECT, EditType.SELECT,
                        "用户角色",
                        "用户的角色", FieldBuilder.EDIT_PLACEHOLDER_SAME_AS_ADD_PLACEHOLDER,
                        FieldRuleListBuilder.create()
                                .add(FieldRuleBuilder.create("角色").required()),
                        "DPD @pick(" + roleListForMock + ")"
                )
                .add("telephone", "telephone", "电话", "",
                        FieldType.TEXT, SearchType.INPUT, AddType.INPUT, EditType.INPUT,
                        "电话号码",
                        "支持11位手机号码、区号-7/8位座机号码", FieldBuilder.EDIT_PLACEHOLDER_SAME_AS_ADD_PLACEHOLDER,
                        FieldRuleListBuilder.create()
                                // .add(FieldRuleBuilder.create("电话").required())
                                .add(FieldRuleBuilder.create("电话").pattern("^(1[3-9]\\d{9})$|^(0\\d{2,3}-?\\d{7,8})$")),
                        "DTD /^(1[3-9]\\d{9})$|^(0\\d{2,3}-?\\d{7,8})$/"
                )
                .build();

        // 指定需要翻译的字段
        ArrayNode fieldMapper = FieldMapperBuilder.create()
                .add("roleId", "roleName", roleMap)
                .build();

        // 拼装返回结果
        ObjectNode map = OBJECT_MAPPER.createObjectNode();
        map.put("total", systemUserPage.getTotal());
        map.set("list", OBJECT_MAPPER.valueToTree(userDtoList));
        map.set("columns", columns);
        map.set("fieldMapper", fieldMapper);
        map.put("idFieldName", idFieldName);
        map.put("pageName", pageName);

        // 返回结果
        return CommonReturn.success(map);
    }

    /**
     * 新增 / 编辑用户
     *
     * @return
     */
    @PostMapping("/edit")
    @ResponseBody
    public CommonReturn edit(HttpServletRequest httpServletRequest, @ModelAttribute UserDto userDTO, String password) {
        // 查询当前登录用户
        User currentLoginUser = systemUserServiceV2.getCurrentLoginUser(httpServletRequest);
        if (currentLoginUser == null) {
            return CommonReturn.error("当前用户未登录");
        } else if (Objects.equals(currentLoginUser.getId(), userDTO.getId())) {
            return CommonReturn.error("不可以修改当前登录账号，如需修改个人账号信息，请前往个人中心进行修改");
        }

        // 传入参数 - 要修改的用户
        User user = UserDto.toEntity(userDTO);

        if (user.getId() == null || user.getId() < 1) {
            // 通过 username 查询系统中是否存在该用户
            User existUser = systemUserServiceV2.getUserByUsername(user.getUsername());

            // 新增用户
            if (existUser != null) {
                return CommonReturn.error("用户名已存在，添加失败");
            }
            if (password == null || password.isEmpty()) {
                return CommonReturn.error("密码不能为空");
            }
            // TODO
            // String passwordHash = DigestUtils.sha512Hex(password);
            // systemUser.setPasswordHash(passwordHash);
            user.setId(null);
            systemUserServiceV2.addUser(user);
        } else {
            // 查询系统中是否存在该用户
            User existUser = systemUserServiceV2.getUserById(user.getId());

            // 修改用户
            if (existUser == null) {
                return CommonReturn.error("用户不存在，操作失败");
            }
            // TODO
            // if (password == null || password.isEmpty()) {
            //     systemUser.setPasswordHash(null);
            // } else {
            //     String passwordHash = DigestUtils.sha512Hex(password);
            //     systemUser.setPasswordHash(passwordHash);
            // }
            systemUserServiceV2.updateUser(user);
        }
        return CommonReturn.success();
    }

    /**
     * 删除用户
     *
     * @param id
     * @return
     */
    @PostMapping("/delete")
    @ResponseBody
    public CommonReturn delete(HttpServletRequest httpServletRequest, Long id) {
        // 先查询用户名是否存在
        User existUser = systemUserServiceV2.getUserById(id);
        if (existUser == null) {
            return CommonReturn.error("用户不存在，删除失败");
        }

        try {
            systemUserServiceV2.deleteUserWithVerify(httpServletRequest.getSession(), existUser.getId());
            return CommonReturn.success();
        } catch (BusinessException e) {
            return CommonReturn.error(e.getErrMsg());
        }
    }

    /**
     * 导出用户列表
     *
     * @return
     */
/*
    @GetMapping("/export")
    @ResponseBody
    @Deprecated
    public CommonReturn exportUserList(UserDto userDTO) {
        List<User> userList = systemUserServiceV2.getUserList(userDTO);
        List<UserDto> userDtoList = UserDto.fromEntity(userList);

        // 当前时间
        Date now = Calendar.getInstance().getTime();
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String dateTime = format.format(now);

        HashMap<String, Object> map = new HashMap<>();
        map.put("list", userDtoList);
        map.put("sheetName", "用户表-" + System.currentTimeMillis());
        map.put("fileName", "用户表_导出时间_" + dateTime);

        return CommonReturn.success(map);
    }
*/
}
