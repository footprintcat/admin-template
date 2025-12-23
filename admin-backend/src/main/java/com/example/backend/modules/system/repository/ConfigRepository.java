package com.example.backend.modules.system.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.backend.common.utils.DateUtils;
import com.example.backend.common.utils.StringUtils;
import com.example.backend.modules.system.enums.config.ConfigScopeEnum;
import com.example.backend.modules.system.mapper.ConfigMapper;
import com.example.backend.modules.system.model.entity.Config;
import jakarta.annotation.Resource;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * <p>
 * 系统设置及临时信息存储表 服务实现类
 * </p>
 *
 * @author coder-xiaomo
 * @since 2025-01-23
 */
@Service
public class ConfigRepository extends ServiceImpl<ConfigMapper, Config> {

    private static final ConfigScopeEnum defaultScope = ConfigScopeEnum.BACKEND;

    @Resource
    private ConfigMapper configMapper;

    public @Nullable String getConfigValue(@NotNull String config) {
        @Nullable Config systemConfig = getConfig(config);
        return systemConfig == null ? null : systemConfig.getValue();
    }

    public @Nullable Integer getConfigValueInteger(@NotNull String config) {
        @Nullable String configValue = getConfigValue(config);
        return StringUtils.isEmpty(configValue) ? null : Integer.parseInt(configValue);
    }

    public @Nullable Double getConfigValueDouble(@NotNull String config) {
        @Nullable String configValue = getConfigValue(config);
        return StringUtils.isEmpty(configValue) ? null : Double.parseDouble(configValue);
    }

    public @Nullable Long getConfigValueLong(@NotNull String config) {
        @Nullable String configValue = getConfigValue(config);
        return StringUtils.isEmpty(configValue) ? null : Long.parseLong(configValue);
    }

    public boolean getConfigValueBoolean(@NotNull String config) {
        @Nullable String configValue = getConfigValue(config);
        return "1".equals(configValue);
    }

    public @NotNull String[] getConfigValues(@NotNull String config) {
        @Nullable Config systemConfig = getConfig(config);
        if (systemConfig == null || systemConfig.getValue() == null) {
            return new String[0];
        }
        return systemConfig.getValue().split(",");
    }

    public @Nullable Config getConfig(@NotNull String config) {
        return getConfig(defaultScope, config);
    }

    public @Nullable Config getConfig(@NotNull ConfigScopeEnum scope, @NotNull String config) {
        Config systemConfig = this.lambdaQuery()
                .eq(Config::getScope, scope)
                .eq(Config::getConfig, config)
                .one();
        if (systemConfig == null) {
            return null;
        }
        if (systemConfig.getExpireTime() != null && DateUtils.toTimestamp(systemConfig.getExpireTime()) < System.currentTimeMillis()) {
            // 配置已过期
            configMapper.deleteById(systemConfig);
            return null;
        }
        return systemConfig;
    }

    public void setConfigBoolean(@NotNull String config, @NotNull Boolean value) {
        setConfig(defaultScope, config, value ? "1" : "0", null);
    }

    public void setConfigDouble(@NotNull String config, double value) {
        setConfig(defaultScope, config, String.valueOf(value), null);
    }

    public void setConfigLong(@NotNull String config, long value) {
        setConfig(defaultScope, config, String.valueOf(value), null);
    }

    public void setConfigLong(@NotNull ConfigScopeEnum scope, @NotNull String config, long value) {
        setConfig(scope, config, String.valueOf(value), null);
    }

    public void setConfig(@NotNull String config, @NotNull String value) {
        setConfig(defaultScope, config, value, null);
    }

    public void setConfig(@NotNull String config, @NotNull String value, @Nullable LocalDateTime expireTime) {
        setConfig(defaultScope, config, value, expireTime);
    }

    public void setConfig(@NotNull ConfigScopeEnum scope, @NotNull String config, @NotNull String value) {
        setConfig(scope, config, value, null);
    }

    /**
     * 设置 config 值
     * <p>
     * 2025.05.19 注意！多线程并发调用可能会出现 Duplicate entry + Deadlock 问题
     *
     * @param scope
     * @param config
     * @param value
     * @param expireTime
     */
    public void setConfig(@NotNull ConfigScopeEnum scope, @NotNull String config, String value, @Nullable LocalDateTime expireTime) {
        Config systemConfig = new Config();
        systemConfig.setConfig(config);
        systemConfig.setScope(scope);
        systemConfig.setValue(value);
        systemConfig.setExpireTime(expireTime);

        // 删除旧配置
        removeConfig(scope, config);

        // 保存新配置
        this.save(systemConfig);
    }

    public void updateConfigValue(@NotNull String config, @NotNull String newValue) {
        updateConfigValue(defaultScope, config, newValue, null);
    }

    public void updateConfigValue(@NotNull String config, @NotNull Long newValue) {
        updateConfigValue(defaultScope, config, newValue);
    }

    public void updateConfigValue(@NotNull ConfigScopeEnum scope, @NotNull String config, @NotNull Long newValue) {
        updateConfigValue(scope, config, String.valueOf(newValue), null);
    }

    /**
     * 更新 config 值
     * <p>
     * 2025.05.19 注意！多线程并发调用可能会出现 Duplicate entry + Deadlock 问题
     *
     * @param scope
     * @param config
     * @param newValue
     * @param expireTimestamp
     */
    public void updateConfigValue(@NotNull ConfigScopeEnum scope, @NotNull String config, @NotNull String newValue, @Nullable Long expireTimestamp) {
        LambdaQueryWrapper<Config> qw = new LambdaQueryWrapper<Config>()
                .eq(Config::getConfig, config)
                .eq(Config::getScope, scope)
                .last("LIMIT 1");
        Config systemConfig = this.getOne(qw);
        if (systemConfig == null) {
            setConfig(config, newValue);
        } else {
            LambdaUpdateWrapper<Config> uw = new LambdaUpdateWrapper<Config>()
                    .set(Config::getValue, newValue)
                    .set(Config::getExpireTime, expireTimestamp)
                    .eq(Config::getConfig, config)
                    .eq(Config::getScope, scope)
                    .last("LIMIT 1");
            configMapper.update(uw);
        }
    }

    /**
     * 删除配置
     *
     * @param scope
     * @param config
     */
    public void removeConfig(@NotNull ConfigScopeEnum scope, @NotNull String config) {
        LambdaQueryWrapper<Config> qw = new LambdaQueryWrapper<>();
        qw.eq(Config::getScope, scope);
        qw.eq(Config::getConfig, config);
        configMapper.delete(qw);
    }

    public void removeConfig(@NotNull String config) {
        removeConfig(defaultScope, config);
    }
}
