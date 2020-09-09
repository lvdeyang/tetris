package com.sumavision.tetris.mims.app.media;

import com.sumavision.tetris.orm.exception.ErrorTypeException;

/**
 * 媒资存储类型<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2018年12月6日 下午1:39:13
 */
public enum StoreType {

	LOCAL("本地存储"),
	REMOTE("远端存储"),
	FTP("ftp存储"),
	HTTP("http存储"),
	THIRDPART("第三方系统");
	
	private String name;
	
	private StoreType(String name){
		this.name = name;
	}
	
	public String getName(){
		return this.name;
	}
	
	public static StoreType fromName(String name) throws Exception{
		StoreType[] values = StoreType.values();
		for(StoreType value:values){
			if(value.getName().equals(name)){
				return value;
			}
		}
		throw new ErrorTypeException("name", name);
	}
	
}
