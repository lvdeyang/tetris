package com.sumavision.tetris.tool.test.flow.client.core.cache.service;

import org.springframework.cache.Cache;
import org.springframework.cache.Cache.ValueWrapper;
import org.springframework.cache.CacheManager;
import com.sumavision.tetris.commons.context.SpringContext;
import com.sumavision.tetris.tool.test.flow.client.core.cache.enumeration.Names;

/**
 * @ClassName: 做一个缓存代理，可以屏蔽外界字符串操作<br/> 
 * @author lvdeyang
 * @date 2018年8月29日 下午3:58:53 
 */
public class MethodResultCache {

	/**
	 * @Title: 清除缓存中的元素<br/> 
	 * @param key 键
	 */
	public static void clear(Object key){
		CacheManager methodResultCacheManager = (CacheManager)SpringContext.getBean(Names.METHOD_RESULT_CACHE_MANAGER.getName());
		Cache c = methodResultCacheManager.getCache(Names.METHOD_RESULT_CACHE_NAME.getName());
		c.evict(key);
	}
	
	/**
	 * @Title: 缓存数据<br/> 
	 * @param key 键
	 * @param value 值
	 */
	public static void put(Object key, Object value){
		CacheManager methodResultCacheManager = (CacheManager)SpringContext.getBean(Names.METHOD_RESULT_CACHE_MANAGER.getName());
		Cache c = methodResultCacheManager.getCache(Names.METHOD_RESULT_CACHE_NAME.getName());
		c.put(key, value);
	}
	
	/**
	 * @Title: 从缓存中拿数据，自动转换Integer类型<br/> 
	 * @param key 键
	 * @return String 值
	 */
	public static Integer getInteger(Object key){
		CacheManager methodResultCacheManager = (CacheManager)SpringContext.getBean(Names.METHOD_RESULT_CACHE_MANAGER.getName());
		Cache c = methodResultCacheManager.getCache(Names.METHOD_RESULT_CACHE_NAME.getName());
		ValueWrapper value = c.get(key);
		if(value == null) return null;
		return Integer.valueOf(value.get().toString());
	}
	
	/**
	 * @Title: 从缓存中拿数据，自动转换String类型<br/> 
	 * @param key 键
	 * @return String 值
	 */
	public static String getString(Object key){
		CacheManager methodResultCacheManager = (CacheManager)SpringContext.getBean(Names.METHOD_RESULT_CACHE_MANAGER.getName());
		Cache c = methodResultCacheManager.getCache(Names.METHOD_RESULT_CACHE_NAME.getName());
		ValueWrapper value = c.get(key);
		if(value == null) return null;
		return value.get().toString();
	}
	
	/**
	 * @Title: 从缓存中拿数据，不做类型转换<br/> 
	 * @param key 键
	 * @return Object 值 
	 */
	public static Object getObject(Object key){
		CacheManager methodResultCacheManager = (CacheManager)SpringContext.getBean(Names.METHOD_RESULT_CACHE_MANAGER.getName());
		Cache c = methodResultCacheManager.getCache(Names.METHOD_RESULT_CACHE_NAME.getName());
		ValueWrapper value = c.get(key);
		if(value == null) return null;
		return value.get();
	}
	
	/**
	 * @Title: 从缓存中拿数据，自定义类型<br/> 
	 * @param key 键
	 * @return Object 值 
	 */
	@SuppressWarnings("unchecked")
	public static <T>T getType(Object key){
		CacheManager methodResultCacheManager = (CacheManager)SpringContext.getBean(Names.METHOD_RESULT_CACHE_MANAGER.getName());
		Cache c = methodResultCacheManager.getCache(Names.METHOD_RESULT_CACHE_NAME.getName());
		ValueWrapper value = c.get(key);
		if(value == null) return null;
		return (T)value.get();
	} 
	
}
