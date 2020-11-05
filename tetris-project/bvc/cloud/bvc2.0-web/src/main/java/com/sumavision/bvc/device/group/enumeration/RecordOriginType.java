package com.sumavision.bvc.device.group.enumeration;

import com.sumavision.tetris.orm.exception.ErrorTypeException;

/**
 * @ClassName: 录制来源类型<br/>
 * @author lvdeyang
 * @date 2018年8月20日 下午2:44:17 
 */
public enum RecordOriginType {

	ROLE("角色"),
	CONFIG("配置");
	
	private String name;
	
	private RecordOriginType(String name){
		this.name = name;
	}
	
	public String getName(){
		return this.name;
	}
	
	/**
	 * @Title: 根据名称获取录制来源类型<br/> 
	 * @param name 名称
	 * @throws Exception  
	 * @return RecordOriginType 录制来源类型
	 */
	public static RecordOriginType fromName(String name) throws Exception{
		RecordOriginType[] values = RecordOriginType.values();
		for(RecordOriginType value:values){
			if(value.getName().equals(name)){
				return value;
			}
		}
		throw new ErrorTypeException("name", name);
	}
	
}
