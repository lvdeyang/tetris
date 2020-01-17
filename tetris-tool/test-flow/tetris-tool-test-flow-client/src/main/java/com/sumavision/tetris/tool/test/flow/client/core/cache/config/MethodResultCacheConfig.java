package com.sumavision.tetris.tool.test.flow.client.core.cache.config;

import java.util.Arrays;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.sumavision.tetris.tool.test.flow.client.core.cache.enumeration.Names;

/**
 * @ClassName: 用来缓存方法返回值<br/> 
 * @Description: 测试过程中修改http返回结果<br/>
 * @author lvdeyang 
 * @date 2018年8月29日 下午3:20:27 
 */
@Configuration
@EnableCaching
public class MethodResultCacheConfig {

	@Bean
    public CacheManager methodResultCacheManager() {
        SimpleCacheManager cacheManager = new SimpleCacheManager();
        //注册一个名为sampleCache的缓存
        cacheManager.setCaches(Arrays.asList(new ConcurrentMapCache(Names.METHOD_RESULT_CACHE_NAME.getName())));
        return cacheManager;
    }
	
}
