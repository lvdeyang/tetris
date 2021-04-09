package com.sumavision.bvc.system.enumeration;

import com.sumavision.tetris.orm.exception.ErrorTypeException;

/**
 * @ClassName: 字典库分类
 * @author MR.Huang
 * @date 2018年8月29日 下午2:53:31 
 */
public enum DicType {

	//对应BO的地区
	REGION("组织"),
	//CLASSIFY("分类"),
	LIVE("直播"),
	DEMAND("点播"),
	//存储位置的区域码给CDN下发命令时使用
	STORAGE_LOCATION("存储位置");
	
	private String name;
	
	private DicType(String name){
		this.name = name;
	}
	
	public String getName(){
		return this.name;
	}
	
	/**
	 * @Title: 根据名称获取创建类型
	 * @param name
	 * @return name 创建类型 
	 */
	public static DicType fromName(String name) throws Exception{
		DicType[] values = DicType.values();
		for(DicType value:values){
			if(value.getName().equals(name)){
				return value;
			}
		}
		throw new ErrorTypeException("name", name);
	}
	
}
