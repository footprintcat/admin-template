package com.example.backend.controller.Manage;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.backend.common.Error.BusinessException;
import com.example.backend.common.PageTable.builder.FieldBuilder;
import com.example.backend.common.PageTable.builder.FieldMapperBuilder;
import com.example.backend.common.PageTable.builder.FieldRuleBuilder;
import com.example.backend.common.PageTable.builder.FieldRuleListBuilder;
import com.example.backend.common.PageTable.enums.AddType;
import com.example.backend.common.PageTable.enums.EditType;
import com.example.backend.common.PageTable.enums.FieldType;
import com.example.backend.common.PageTable.enums.SearchType;
import com.example.backend.common.Response.CommonReturnType;
import com.example.backend.controller.base.BaseController;
import com.example.backend.dto.UserDTO;
import com.example.backend.entity.User;
import com.example.backend.query.PageQuery;
import com.example.backend.service.v2.RoleServiceV2;
import com.example.backend.service.v2.UserServiceV2;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.codec.digest.DigestUtils;
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
import java.util.Objects;
import java.util.stream.Collectors;

@CrossOrigin
@RestController
@RequestMapping("/v2/manage/user")
public class ManageUserController extends BaseController {

    @Resource
    private UserServiceV2 userServiceV2;
    @Resource
    private RoleServiceV2 roleServiceV2;

    /**
     * 获取用户列表
     *
     * @param pageQuery 分页参数
     * @param userDTO   筛选条件
     * @return
     */
    @GetMapping("/list")
    @ResponseBody
    public CommonReturnType list(PageQuery pageQuery, UserDTO userDTO) {
        // 查询分页数据
        Page<User> userPage = userServiceV2.getUserPageWithoutSuAccount(pageQuery, userDTO);

        // 查询 roleMap
        HashMap<Integer, String> roleMap = roleServiceV2.getRoleMap();

        // 用于 role 下拉框 mock 数据
        String roleListForMock = JSONArray.from(roleMap.keySet().stream().map(Object::toString).collect(Collectors.toList())).toString();

        // 分页数据转为 DTO
        List<User> userList = userPage.getRecords();
        List<UserDTO> userDTOList = UserDTO.fromEntity(userList);

        // id列 字段名（区分大小写；以VO中的变量名为准）
        // 新增、修改弹窗时，使用该列作为主键列进行操作
        String idFieldName = "id";

        // 当前管理页面
        String pageName = "用户管理";

        // 指定前端表格显示列
        JSONArray columns = FieldBuilder.create()
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
        JSONArray fieldMapper = FieldMapperBuilder.create()
                .add("roleId", "roleName", roleMap)
                .build();

        // 拼装返回结果
        JSONObject map = new JSONObject();
        map.put("total", userPage.getTotal());
        map.put("list", userDTOList);
        map.put("columns", columns);
        map.put("fieldMapper", fieldMapper);
        map.put("idFieldName", idFieldName);
        map.put("pageName", pageName);

        // 返回结果
        return CommonReturnType.success(map);
    }

    /**
     * 新增 / 编辑用户
     *
     * @return
     */
    @PostMapping("/edit")
    @ResponseBody
    public CommonReturnType edit(HttpServletRequest httpServletRequest, @ModelAttribute UserDTO userDTO, String password) {
        // 查询当前登录用户
        User currentLoginUser = userServiceV2.getCurrentLoginUser(httpServletRequest);
        if (currentLoginUser == null) {
            return CommonReturnType.error("当前用户未登录");
        } else if (Objects.equals(String.valueOf(currentLoginUser.getId()), userDTO.getId())) {
            return CommonReturnType.error("不可以修改当前登录账号，如需修改个人账号信息，请前往个人中心进行修改");
        }

        // 传入参数 - 要修改的用户
        User user = UserDTO.toEntity(userDTO);

        if (user.getId() == null || user.getId() < 1) {
            // 通过 username 查询系统中是否存在该用户
            User existUser = userServiceV2.getUserByUsername(user.getUsername());

            // 新增用户
            if (existUser != null) {
                return CommonReturnType.error("用户名已存在，添加失败");
            }
            if (password == null || password.isEmpty()) {
                return CommonReturnType.error("密码不能为空");
            }
            String passwordHash = DigestUtils.sha512Hex(password);
            user.setPassword(passwordHash);
            user.setId(null);
            userServiceV2.addUser(user);
        } else {
            // 查询系统中是否存在该用户
            User existUser = userServiceV2.getUserById(user.getId());

            // 修改用户
            if (existUser == null) {
                return CommonReturnType.error("用户不存在，操作失败");
            }
            if (password == null || password.isEmpty()) {
                user.setPassword(null);
            } else {
                String passwordHash = DigestUtils.sha512Hex(password);
                user.setPassword(passwordHash);
            }
            userServiceV2.updateUser(user);
        }
        return CommonReturnType.success();
    }

    /**
     * 删除用户
     *
     * @param id
     * @return
     */
    @PostMapping("/delete")
    @ResponseBody
    public CommonReturnType delete(HttpServletRequest httpServletRequest, Long id) {
        // 先查询用户名是否存在
        User existUser = userServiceV2.getUserById(id);
        if (existUser == null) {
            return CommonReturnType.error("用户不存在，删除失败");
        }

        try {
            userServiceV2.deleteUserWithVerify(httpServletRequest.getSession(), existUser.getId());
            return CommonReturnType.success();
        } catch (BusinessException e) {
            return CommonReturnType.error(e.getErrMsg());
        }
    }

    /**
     * 导出用户列表
     *
     * @return
     */
    @GetMapping("/export")
    @ResponseBody
    public CommonReturnType exportUserList(UserDTO userDTO) {
        List<User> userList = userServiceV2.getUserListWithoutSuAccount(userDTO);
        List<UserDTO> userDTOList = UserDTO.fromEntity(userList);

        // 当前时间
        Date now = Calendar.getInstance().getTime();
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String dateTime = format.format(now);

        HashMap<String, Object> map = new HashMap<>();
        map.put("list", userDTOList);
        map.put("sheetName", "用户表-" + System.currentTimeMillis());
        map.put("fileName", "用户表_导出时间_" + dateTime);

        return CommonReturnType.success(map);
    }
}
