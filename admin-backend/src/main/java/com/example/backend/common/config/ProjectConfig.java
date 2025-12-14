package com.example.backend.common.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "project-config", ignoreInvalidFields = true)
@Data
public class ProjectConfig {

}
