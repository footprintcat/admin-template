package com.example.backend.common.scheduletask;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
// 通过配置文件指定是否开启定时任务（默认不开启）
@ConditionalOnExpression("${project-config.schedule-task.enable:false}")
public class StaffAttendanceWarningScheduleTask {

    // @Scheduled(cron = "*/1 * * * * ?")
    // public void sayWord() {
    //     System.out.println("world");
    // }

    // 本地测试使用
    // @Scheduled(initialDelay = 0, fixedRate = 60 * 1000)
    // 线上环境使用
    @Scheduled(initialDelay = 20 * 1000, fixedRate = 5 * 60 * 1000)
    // 容器启动后,延迟20秒后再执行一次定时器,以后每5分钟再执行一次该定时器。
    // fixedRate:是按照一定的速率执行，是从上一次方法执行开始的时间算起，如果上一次方法阻塞住了，下一次也是不会执行，但是在阻塞这段时间内累计应该执行的次数，当不再阻塞时，一下子把这些全部执行掉，而后再按照固定速率继续执行。
    public void getNeedWarningWorkerList() {
        long currentTimeMillis = System.currentTimeMillis();
        log.info("当前时间: {}", currentTimeMillis);
    }

}
