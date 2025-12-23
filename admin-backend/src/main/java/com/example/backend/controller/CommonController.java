package com.example.backend.controller;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.example.backend.common.annotations.HandleControllerGlobalException;
import com.example.backend.common.baseobject.response.CommonReturn;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 无需登录即可访问的公用方法
 */
@HandleControllerGlobalException
@RestController
@RequestMapping("/common")
public class CommonController {

    @GetMapping("/getSnowId")
    public CommonReturn getSnowId() {
        return CommonReturn.success(IdWorker.getIdStr());
    }
}
