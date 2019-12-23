package com.sumavision.tetris.sts.common;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConstantUtil {

    @Value("${spring.cloud.client.ipAddress:127.0.0.1}")//application.yml文件内配置数据源的前缀
    private String serverIp;

    @Value("${server.port:80}")
    private Integer serverPort;

    public String getServerIp() {
        return serverIp;
    }

    public Integer getServerPort() {
        return serverPort;
    }
}
