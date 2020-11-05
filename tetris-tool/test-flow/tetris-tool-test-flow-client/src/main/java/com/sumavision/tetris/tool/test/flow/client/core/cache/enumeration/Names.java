package com.sumavision.tetris.tool.test.flow.client.core.cache.enumeration;

import com.sumavision.tetris.tool.test.flow.client.core.cache.exception.ErrorNamesTypeException;

/**
 * @ClassName: 缓存相关名称常量<br/> 
 * @author lvdeyang
 * @date 2018年8月29日 下午5:07:34 
 */
public enum Names {

	/** cacheManager 名称 */
	METHOD_RESULT_CACHE_MANAGER("methodResultCacheManager"),
	
	/** cache名称 */
	METHOD_RESULT_CACHE_NAME("methodResultCache"),
	
	/** 方法返回结果返回的key */
	METHOD_RESULT_KEY("methodResultKey");
	
	private String name;
	
	private Names(String name){
		this.name = name;
	}
	
	public String getName(){
		return this.name;
	}
	
	/**
	 * @Title: 根据名称获取枚举<br/> 
	 * @param name 名称
	 * @throws Exception
	 * @return Names 枚举
	 */
	public static Names fromName(String name) throws Exception{
		Names[] values = Names.values();
		for(Names value:values){
			if(value.getName().equals(name)){
				return value;
			}
		}
		throw new ErrorNamesTypeException(name);
	}
	
}
