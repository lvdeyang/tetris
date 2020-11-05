package com.sumavision.tetris.zoom;

import com.sumavision.tetris.orm.exception.ErrorTypeException;

/**
 * 资源分组<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2020年3月2日 上午10:04:57
 */
public enum SourceGroupType {

	FAVORITES("收藏夹"),
	HISTORY("历史记录"),
	CONTACTS("联系人");
	
	private String name;
	
	private SourceGroupType(String name){
		this.name = name;
	}
	
	public String getName(){
		return this.name;
	}
	
	public static SourceGroupType fromName(String name) throws Exception{
		SourceGroupType[] values = SourceGroupType.values();
		for(SourceGroupType value:values){
			if(value.getName().equals(name)){
				return value;
			}
		}
		throw new ErrorTypeException("name", name);
	}
	
}
