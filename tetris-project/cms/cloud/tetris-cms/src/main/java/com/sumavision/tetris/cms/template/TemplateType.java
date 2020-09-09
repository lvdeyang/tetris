package com.sumavision.tetris.cms.template;

import org.omg.CORBA.INTERNAL;

import com.sumavision.tetris.orm.exception.ErrorTypeException;

/**
 * 内容模板分类<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2019年2月14日 下午5:10:57
 */
public enum TemplateType {

	ARTICLE("文章排版"),
	INTERNAL("内置模板");
	
	private String name;
	
	private TemplateType(String name){
		this.name = name;
	}
	
	public String getName(){
		return this.name;
	}
	
	public static TemplateType fromName(String name) throws Exception{
		TemplateType[] values = TemplateType.values();
		for(TemplateType value:values){
			if(value.getName().equals(name)){
				return value;
			}
		}
		throw new ErrorTypeException("name", name);
	}
	
}
