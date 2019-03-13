package com.sumavision.tetris.lib.aliyun.push;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Properties;import org.aspectj.weaver.NewConstructorTypeMunger;
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
        client = new DefaultAcsClient(profile);
        return client;
    }
    
    public static void main(String[] args){
    	SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US);
    	System.out.println(sf.format(new Date()));
    }
}