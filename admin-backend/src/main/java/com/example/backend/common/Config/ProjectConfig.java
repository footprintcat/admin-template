package com.example.backend.common.Config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "project-config", ignoreInvalidFields = true)
@Data
public class ProjectConfig {

}
