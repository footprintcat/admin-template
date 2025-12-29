package com.example.backend.controller.manage.v1.needrefactor;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.backend.common.PageTable.builder.FieldBuilder;
import com.example.backend.common.PageTable.builder.FieldMapperBuilder;
import com.example.backend.common.PageTable.builder.FieldRuleListBuilder;
import com.example.backend.common.PageTable.enums.AddType;
import com.example.backend.common.PageTable.enums.EditType;
import com.example.backend.common.PageTable.enums.FieldType;
import com.example.backend.common.PageTable.enums.SearchType;
import com.example.backend.common.annotations.HandleControllerGlobalException;
import com.example.backend.common.baseobject.request.PageQuery;
import com.example.backend.common.baseobject.response.CommonReturn;
import com.example.backend.modules.system.model.converter.LogConverter;
import com.example.backend.modules.system.model.dto.LogDto;
import com.example.backend.modules.system.model.entity.Log;
import com.example.backend.modules.system.service.needrefactor.SystemLogService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Slf4j
@HandleControllerGlobalException
@RestController
@RequestMapping("/manage/v1/system/log")
@Tag(name = "[system] 日志 log", description = "/manage/v1/system/log")
public class ManageSystemLogController {

    @Resource
    private LogConverter logConverter;
    @Resource
    private SystemLogService systemLogService;

    @GetMapping("/list")
    public CommonReturn list(@ModelAttribute PageQuery pageQuery, LogDto logDTO) {
        // 查询分页数据
        Page<Log> systemLogPage = systemLogService.getSystemLogPage(pageQuery, logDTO);

        // 分页数据转为 DTO
        List<Log> logList = systemLogPage.getRecords();
        List<LogDto> logDtoList = logConverter.toDto(logList);

        // id列 字段名（区分大小写；以VO中的变量名为准）
        // 新增、修改弹窗时，使用该列作为主键列进行操作
        String idFieldName = "id";

        // 当前管理页面
        String pageName = "系统日志管理";

        // 指定前端表格显示列
        JSONArray columns = FieldBuilder.create()
                .add("createTimestamp", "createTimestamp", "产生时间", "",
                        FieldType.DATETIME, SearchType.CAN_NOT_SEARCH, AddType.CAN_NOT_ADD, EditType.CAN_NOT_EDIT,
                        FieldBuilder.SEARCH_PLACEHOLDER_SAME_AS_FIELDNAME, "", "",
                        FieldRuleListBuilder.create(), null
                )
                .add("eventType", "eventType", "事件类型", "",
                        FieldType.TEXT, SearchType.INPUT, AddType.CAN_NOT_ADD, EditType.CAN_NOT_EDIT,
                        FieldBuilder.SEARCH_PLACEHOLDER_SAME_AS_FIELDNAME, "", "",
                        FieldRuleListBuilder.create(), null
                )
                .add("userId", "userId", "用户ID", "",
                        FieldType.TEXT, SearchType.INPUT, AddType.CAN_NOT_ADD, EditType.CAN_NOT_EDIT,
                        FieldBuilder.SEARCH_PLACEHOLDER_SAME_AS_FIELDNAME, "", "",
                        FieldRuleListBuilder.create(), null
                )
                .add("userName", "userName", "用户名", "",
                        FieldType.TEXT, SearchType.INPUT, AddType.CAN_NOT_ADD, EditType.CAN_NOT_EDIT,
                        FieldBuilder.SEARCH_PLACEHOLDER_SAME_AS_FIELDNAME, "", "",
                        FieldRuleListBuilder.create(), null
                )
                .add("ip", "ip", "IP", "",
                        FieldType.TEXT, SearchType.INPUT, AddType.CAN_NOT_ADD, EditType.CAN_NOT_EDIT,
                        FieldBuilder.SEARCH_PLACEHOLDER_SAME_AS_FIELDNAME, "", "",
                        FieldRuleListBuilder.create(), null
                )
                .add("title", "title", "日志", "",
                        FieldType.TEXT, SearchType.INPUT, AddType.CAN_NOT_ADD, EditType.CAN_NOT_EDIT,
                        FieldBuilder.SEARCH_PLACEHOLDER_SAME_AS_FIELDNAME, "", "",
                        FieldRuleListBuilder.create(), null
                )
                .add("content", "content", "详情", "",
                        FieldType.TEXT, SearchType.INPUT, AddType.CAN_NOT_ADD, EditType.CAN_NOT_EDIT,
                        FieldBuilder.SEARCH_PLACEHOLDER_SAME_AS_FIELDNAME, "", "",
                        FieldRuleListBuilder.create(), null
                )
                .build();

        // 指定需要翻译的字段
        JSONArray fieldMapper = FieldMapperBuilder.create()
                .build();

        // 拼装返回结果
        JSONObject map = new JSONObject();
        map.put("total", systemLogPage.getTotal());
        map.put("list", logDtoList);
        map.put("columns", columns);
        map.put("fieldMapper", fieldMapper);
        map.put("idFieldName", idFieldName);
        map.put("pageName", pageName);

        // 返回结果
        return CommonReturn.success(map);
    }

    /**
     * 开放记录日志接口
     *
     * @param params
     * @param request 请求参数
     * @return
     */
    // @PostMapping("/add")
    // public CommonReturn dataProcessAdd(@RequestBody JSONObject params, HttpServletRequest request) {
    //     String ipAddr = IPUtils.getIpAddr(request);
    //
    //     SystemLog systemLog = new SystemLog();
    //     systemLog.setAction(params.getString("action"));
    //     systemLog.setContent(params.getString("content"));
    //     systemLog.setIp(ipAddr);
    //     systemLog.setTitle(params.getString("title"));
    //     systemLog.setUserId(null);
    //     systemLogService.add(systemLog);
    //
    //     return CommonReturn.success();
    // }

    /**
     * 导出列表
     *
     * @return
     */
    @GetMapping("/export")
    @ResponseBody
    public CommonReturn exportSystemLogList(LogDto logDTO) {
        List<Log> logList = systemLogService.getSystemLogList(logDTO);
        List<LogDto> logDtoList = logConverter.toDto(logList);

        // 当前时间
        Date now = Calendar.getInstance().getTime();
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String dateTime = format.format(now);

        HashMap<String, Object> map = new HashMap<>();
        map.put("list", logDtoList);
        map.put("sheetName", "设备表-" + System.currentTimeMillis());
        map.put("fileName", "设备表_导出时间_" + dateTime);

        return CommonReturn.success(map);
    }

}
