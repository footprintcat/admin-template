package com.example.backend.common.utils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * LocalDateTime 与时间戳 timestamp 互相转换
 *
 * @since 2025-12-24
 */
public class DateUtils {

    private static final ZoneId zone = ZoneId.systemDefault();

    /**
     * timestamp 转 LocalDateTime
     *
     * @param timestamp 毫秒级时间戳
     * @return LocalDateTime
     */
    public static LocalDateTime toLocalDateTime(long timestamp) { // timestampToLocalDateTime
        Instant instant = Instant.ofEpochMilli(timestamp);
        return LocalDateTime.ofInstant(instant, zone);
    }

    /**
     * 获取指定日期的毫秒
     *
     * @param ldt LocalDateTime
     * @return 毫秒级时间戳
     */
    public static long toTimestamp(LocalDateTime ldt) { // localDateTimeToTimestamp
        // long timestamp = ldt.toInstant(ZoneOffset.of("+8")).toEpochMilli();
        return ldt.atZone(zone).toInstant().toEpochMilli();
    }
}
