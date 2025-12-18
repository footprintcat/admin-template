package com.example.backend;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;

public class SnowflakeIdTest {
    /**
     * 生成若干雪花id
     *
     * @param args
     * @throws InterruptedException
     */
    public static void main(String[] args) throws InterruptedException {
        for (int i = 0; i < 100; i++) {
            long snowflakeId = IdWorker.getId();
            System.out.println(snowflakeId);
            Thread.sleep(10);
        }
    }
}
