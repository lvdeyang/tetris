package com.sumavision.tetris.cms.aliPush;

import java.io.InputStream;
import java.util.Properties;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;

@Configuration
public class BasePush {

	protected static String region;
    protected static long appKey;
    protected static String androidActivity;
    protected static String signName;
    protected static String templateCode;

    protected static DefaultAcsClient client;

    /**
     * 从配置文件中读取配置值，初始化Client
     * <p>
     * 1. 获取 accessKeyId/accessKeySecret/appKey <br/>
     * 2. 先在 push.properties 配置文件中 填入你的获取的值
     */
    @Bean
    public static DefaultAcsClient beforeClass() throws Exception {
        InputStream inputStream = BasePush.class.getClassLoader().getResourceAsStream("push.properties");
        Properties properties = new Properties();
        properties.load(inputStream);

        String accessKeyId = properties.getProperty("accessKeyId");

        String accessKeySecret = properties.getProperty("accessKeySecret");

        String key = properties.getProperty("appKey");

        region = properties.getProperty("regionId");
        appKey = Long.valueOf(key);
        androidActivity = properties.getProperty("androidActivity");
        signName = properties.getProperty("signName");
        templateCode = properties.getProperty("templateCode");

        IClientProfile profile = DefaultProfile.getProfile(region, accessKeyId, accessKeySecret);
        DefaultProfile.addEndpoint(region, region, "Push", "cloudPush.aliyuncs.com");
        client = new DefaultAcsClient(profile);
        return client;
    }
}