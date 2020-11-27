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
    protected static String accessKeyId;
    protected static String accessKeySecret;
    protected static String notificaitonchannel;
    
    protected static long iosAppKey;
    protected static String iosAccessKeyId;
    protected static String iosAccessKeySecret;

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

        accessKeyId = properties.getProperty("accessKeyId");

        accessKeySecret = properties.getProperty("accessKeySecret");
        
        iosAccessKeyId = properties.getProperty("iosAccessKeyId");

        iosAccessKeySecret = properties.getProperty("iosAccessKeySecret");

        String key = properties.getProperty("appKey");
        iosAppKey=(Long.valueOf(properties.getProperty("iosAppKey")));

        region = properties.getProperty("regionId");
        appKey = Long.valueOf(key);
        androidActivity = properties.getProperty("androidActivity");
        signName = properties.getProperty("signName");
        templateCode = properties.getProperty("templateCode");
        notificaitonchannel = properties.getProperty("notificaitonchannel");

        IClientProfile profile = DefaultProfile.getProfile(region, accessKeyId, accessKeySecret);
        client = new DefaultAcsClient(profile);
        return client;
    }
    
    public static void main(String[] args){
    	SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US);
    	System.out.println(sf.format(new Date()));
    }
}