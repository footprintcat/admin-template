package com.example.backend.common.ScheduleTask;

import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.support.PeriodicTrigger;
import org.springframework.stereotype.Component;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

@Component
public class TaskSchedulerDemo {

    private final TaskScheduler taskScheduler;
    private ScheduledFuture<?> scheduledFuture;

    public TaskSchedulerDemo(TaskScheduler taskScheduler) {
        this.taskScheduler = taskScheduler;
    }

    public void startPolling(long spanSecond) {
        // 创建一个定时任务，每隔 spanSecond 秒执行一次 checkSmsStatus 方法
        PeriodicTrigger trigger = new PeriodicTrigger(spanSecond, TimeUnit.SECONDS);
        scheduledFuture = taskScheduler.schedule(this::checkSmsStatus, trigger);
    }

    public void stopPolling() {
        if (scheduledFuture != null) {
            scheduledFuture.cancel(true);
        }
    }

    int i = 0;

    private void checkSmsStatus() {
        // 在这里轮询查询结果
        // ...
        System.out.println("轮询");
        if (++i > 5) {
            stopPolling();
        }
    }
}