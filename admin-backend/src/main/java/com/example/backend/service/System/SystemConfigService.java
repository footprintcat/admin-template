package com.example.backend.service.System;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.example.backend.common.Utils.StringUtils;
import com.example.backend.entity.SystemConfig;
import com.example.backend.mapper.SystemConfigMapper;
import com.example.backend.repository.SystemConfigRepository;
import jakarta.annotation.Resource;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Service;

@Service
public class SystemConfigService {

    private static final String defaultOwner = "backend";

    @Resource
    private SystemConfigRepository systemConfigRepository;
    @Resource
    private SystemConfigMapper systemConfigMapper;

    public String getConfigValue(String config) {
        SystemConfig systemConfig = getConfig(config);
        return systemConfig == null ? null : systemConfig.getValue();
    }

    public Integer getConfigValueInteger(String config) {
        String configValue = getConfigValue(config);
        return StringUtils.isEmpty(configValue) ? null : Integer.parseInt(configValue);
    }

    public Double getConfigValueDouble(String config) {
        String configValue = getConfigValue(config);
        return StringUtils.isEmpty(configValue) ? null : Double.parseDouble(configValue);
    }

    public Long getConfigValueLong(String config) {
        String configValue = getConfigValue(config);
        return StringUtils.isEmpty(configValue) ? null : Long.parseLong(configValue);
    }

    public boolean getConfigValueBoolean(String config) {
        String configValue = getConfigValue(config);
        return "1".equals(configValue);
    }

    public String[] getConfigValues(String config) {
        SystemConfig systemConfig = getConfig(config);
        if (systemConfig == null || systemConfig.getValue() == null) {
            return new String[0];
        }
        return systemConfig.getValue().split(",");
    }

    public SystemConfig getConfig(String config) {
        return getConfig(defaultOwner, config);
    }

    public SystemConfig getConfig(String owner, String config) {
        SystemConfig systemConfig = systemConfigRepository.lambdaQuery()
                .eq(SystemConfig::getOwner, owner)
                .eq(SystemConfig::getConfig, config)
                .one();
        if (systemConfig == null) {
            return null;
        }
        if (systemConfig.getExpireTimestamp() != null && systemConfig.getExpireTimestamp() < System.currentTimeMillis()) {
            // 配置已过期
            systemConfigMapper.deleteById(systemConfig);
            return null;
        }
        return systemConfig;
    }

    public void setConfigBoolean(@NotNull String config, @NotNull Boolean value) {
        setConfig(defaultOwner, config, value ? "1" : "0", null);
    }

    public void setConfigDouble(String config, double value) {
        setConfig(defaultOwner, config, String.valueOf(value), null);
    }

    public void setConfigLong(String config, long value) {
        setConfig(defaultOwner, config, String.valueOf(value), null);
    }

    public void setConfigLong(String owner, String config, long value) {
        setConfig(owner, config, String.valueOf(value), null);
    }

    public void setConfig(String config, String value) {
        setConfig(defaultOwner, config, value, null);
    }

    public void setConfig(String config, String value, Long expireTimestamp) {
        setConfig(defaultOwner, config, value, expireTimestamp);
    }

    public void setConfig(String owner, String config, String value) {
        setConfig(owner, config, value, null);
    }

    /**
     * 设置 config 值
     * <p>
     * 2025.05.19 注意！多线程并发调用可能会出现 Duplicate entry + Deadlock 问题
     *
     * @param owner
     * @param config
     * @param value
     * @param expireTimestamp
     */
    public void setConfig(@NotNull String owner, @NotNull String config, String value, @Nullable Long expireTimestamp) {
        SystemConfig systemConfig = new SystemConfig();
        systemConfig.setConfig(config);
        systemConfig.setOwner(owner);
        systemConfig.setValue(value);
        systemConfig.setExpireTimestamp(expireTimestamp);

        // 删除旧配置
        removeConfig(owner, config);

        // 保存新配置
        systemConfigRepository.save(systemConfig);
    }

    public void updateConfigValue(@NotNull String config, @NotNull String newValue) {
        updateConfigValue(defaultOwner, config, newValue, null);
    }

    public void updateConfigValue(@NotNull String config, @NotNull Long newValue) {
        updateConfigValue(defaultOwner, config, newValue);
    }

    public void updateConfigValue(@NotNull String owner, @NotNull String config, @NotNull Long newValue) {
        updateConfigValue(owner, config, String.valueOf(newValue), null);
    }

    /**
     * 更新 config 值
     * <p>
     * 2025.05.19 注意！多线程并发调用可能会出现 Duplicate entry + Deadlock 问题
     *
     * @param owner
     * @param config
     * @param newValue
     * @param expireTimestamp
     */
    public void updateConfigValue(@NotNull String owner, @NotNull String config, @NotNull String newValue, @Nullable Long expireTimestamp) {
        LambdaQueryWrapper<SystemConfig> qw = new LambdaQueryWrapper<SystemConfig>()
                .eq(SystemConfig::getConfig, config)
                .eq(SystemConfig::getOwner, owner)
                .last("LIMIT 1");
        SystemConfig systemConfig = systemConfigRepository.getOne(qw);
        if (systemConfig == null) {
            setConfig(config, newValue);
        } else {
            LambdaUpdateWrapper<SystemConfig> uw = new LambdaUpdateWrapper<SystemConfig>()
                    .set(SystemConfig::getValue, newValue)
                    .set(SystemConfig::getExpireTimestamp, expireTimestamp)
                    .eq(SystemConfig::getConfig, config)
                    .eq(SystemConfig::getOwner, owner)
                    .last("LIMIT 1");
            systemConfigMapper.update(uw);
        }
    }

    /**
     * 删除配置
     *
     * @param owner
     * @param config
     */
    public void removeConfig(@NotNull String owner, @NotNull String config) {
        LambdaQueryWrapper<SystemConfig> qw = new LambdaQueryWrapper<>();
        qw.eq(SystemConfig::getConfig, config);
        qw.eq(SystemConfig::getOwner, owner);
        systemConfigMapper.delete(qw);
    }

    public void removeConfig(@NotNull String config) {
        removeConfig(defaultOwner, config);
    }
}
