package com.sumavision.signal.bvc.entity.enumeration;

import com.sumavision.tetris.orm.exception.ErrorTypeException;

/**
 * 网口类型<br/>
 * <b>作者:</b>wjw<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2019年5月20日 下午1:18:57
 */
public enum InternetAccessType {

	STREAM_OUT("输出"),
	STREAM_IN("输入");
	
	private String name;
	
	private InternetAccessType(String name){
		this.name = name;
	}

	public String getName() {
		return name;
	}
	
	/**
	 * @Title: 根据网口类型名称获取类型 
	 * @param name 名称
	 * @throws Exception 
	 * @return InternetAccessType 议程音频类型
	 */
	public static final InternetAccessType fromName(String name) throws Exception{
		InternetAccessType[] values = InternetAccessType.values();
		for(InternetAccessType value:values){
			if(value.getName().equals(name)){
				return value;
			}
		}
		throw new ErrorTypeException("name", name);
	}
	
}
