package com.example.backend.common.Utils;

import com.alibaba.fastjson2.util.IOUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@Component
public class ReadFile {
    // public static String loadFromFile(String fileName) throws IOException {
    //     return new String(Files.readAllBytes(Paths.get("src/main/resources/old-json/" + fileName)));
    // }

    public static String loadFromFile(String fileName) throws IOException {
        ClassPathResource resource = new ClassPathResource("old-json/" + fileName);
        InputStream inputStream = resource.getInputStream();

        byte[] bytes = inputStream.readAllBytes();
        return new String(bytes, StandardCharsets.UTF_8);
    }
}
