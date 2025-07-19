package com.example.backend;

import com.example.backend.common.ScheduleTask.TaskSchedulerDemo;
import jakarta.annotation.Resource;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@SpringBootTest(classes = BackendApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TaskSchedulerTest {

    @Resource
    TaskSchedulerDemo taskSchedulerDemo;

    @SneakyThrows
    @Test
    public void test() {
        taskSchedulerDemo.startPolling(1);

        System.out.println("开始阻塞");
        // 在定时任务执行完毕后调用latch.countDown()方法
        CountDownLatch latch = new CountDownLatch(1);
        // 阻塞主线程，等待定时任务执行完毕
        latch.await(10, TimeUnit.SECONDS);
        System.out.println("阻塞结束，马上退出");
    }
}
