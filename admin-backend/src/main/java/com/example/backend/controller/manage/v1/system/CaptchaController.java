package com.example.backend.controller.manage.v1.system;

import com.example.backend.common.annotations.HandleControllerGlobalException;
import com.example.backend.common.annotations.PublicAccess;
import com.example.backend.common.utils.CaptchaGenerator;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 * 验证码控制器
 *
 * @author AI Assistant
 */
@Slf4j
@HandleControllerGlobalException
@RestController
@RequestMapping("/manage/v1/system/captcha")
@Tag(name = "[system] 验证码 captcha", description = "/manage/v1/system/captcha")
public class CaptchaController {

    @Resource
    private CaptchaGenerator captchaGenerator;

    /**
     * 生成验证码图片
     *
     * @return 验证码图片（PNG格式）
     * @throws IOException
     */
    @PublicAccess
    @Operation(summary = "生成验证码图片", description = "生成验证码图片并返回PNG格式图片，验证码文本保存在Session中")
    @GetMapping(value = "/image", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<byte[]> getCaptchaImage() throws IOException {
        byte[] captchaImage = captchaGenerator.generateCaptcha();
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_PNG)
                .header("Cache-Control", "no-cache, no-store, must-revalidate")
                .header("Pragma", "no-cache")
                .header("Expires", "0")
                .body(captchaImage);
    }
}
