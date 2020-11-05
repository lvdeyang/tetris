package com.sumavision.tetris.tool.test.flow.client.core.rmi.config;

import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

/**
 * @ClassName: 远程调用启动配置<br/> 
 * @author lvdeyang
 * @date 2018年8月29日 下午7:40:39 
 */
@Configuration
@EnableFeignClients(basePackages={"com.sumavision.tetris.tool.test.flow.client.core.rmi"})
public class EnableTestFlowFeignConfig {

}
