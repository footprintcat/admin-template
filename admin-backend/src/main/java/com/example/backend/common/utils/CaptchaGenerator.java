package com.example.backend.common.utils;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Random;

/**
 * 验证码生成工具类
 *
 * @author AI Assistant
 */
@Component
public class CaptchaGenerator {

    @Resource
    private HttpSession httpSession;

    // 验证码字符集，排除容易混淆的字符
    private static final String CHAR_SET = "1234567890";
    // private static final String CHAR_SET = "23456789ABCDEFGHJKLMNPQRSTUVWXYZ";
    private static final int WIDTH = 120;
    private static final int HEIGHT = 40;
    private static final int CODE_LENGTH = 4;
    private static final int LINE_COUNT = 20;
    private static final String SESSION_KEY = "captcha";

    /**
     * 生成验证码图片并保存到Session
     *
     * @return 包含验证码图片的字节数组
     * @throws IOException
     */
    public byte[] generateCaptcha() throws IOException {
        // 1. 创建图片缓冲区
        BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = image.createGraphics();

        // 2. 设置背景色
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, WIDTH, HEIGHT);

        // 3. 设置字体
        g.setFont(new Font("Arial", Font.BOLD, 24));

        // 4. 生成随机验证码
        Random random = new Random();
        StringBuilder captchaText = new StringBuilder();

        for (int i = 0; i < CODE_LENGTH; i++) {
            char c = CHAR_SET.charAt(random.nextInt(CHAR_SET.length()));
            captchaText.append(c);

            // 随机颜色
            g.setColor(new Color(
                    random.nextInt(150),
                    random.nextInt(150),
                    random.nextInt(150)
            ));

            // 绘制字符（添加轻微旋转）
            double theta = (random.nextDouble() - 0.5) * 0.3; // ±0.3弧度
            g.rotate(theta, 20 + i * 25, 20);
            g.drawString(String.valueOf(c), 15 + i * 25, 25);
            g.rotate(-theta, 20 + i * 25, 20);
        }

        // 5. 添加干扰线
        g.setColor(Color.LIGHT_GRAY);
        for (int i = 0; i < LINE_COUNT; i++) {
            int x1 = random.nextInt(WIDTH);
            int y1 = random.nextInt(HEIGHT);
            int x2 = random.nextInt(WIDTH);
            int y2 = random.nextInt(HEIGHT);
            g.drawLine(x1, y1, x2, y2);
        }

        // 6. 添加噪点
        for (int i = 0; i < 100; i++) {
            int x = random.nextInt(WIDTH);
            int y = random.nextInt(HEIGHT);
            image.setRGB(x, y, random.nextInt(0xFFFFFF));
        }

        g.dispose();

        // 7. 保存验证码到Session
        String captchaCode = captchaText.toString();
        httpSession.setAttribute(SESSION_KEY, captchaCode);

/*
        // 打印 session 中所有属性，仅调试用
        Enumeration<String> attributeNames = httpSession.getAttributeNames();
        while (attributeNames.hasMoreElements()) {
            String attributeName = attributeNames.nextElement();
            System.out.println(attributeName + ": " + httpSession.getAttribute(attributeName));
        }
        System.out.println("");
*/

        // 8. 转换为字节数组
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, "png", baos);

        return baos.toByteArray();
    }
}
