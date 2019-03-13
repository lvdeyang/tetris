package com.sumavision.tetris.cms.template;

import com.sumavision.tetris.orm.exception.ErrorTypeException;

public enum TemplateId {

	YJGB_COMMOM("应急广播通用", "yjgb_commom"),
	YJGB_TITLE("应急广播标题", "yjgb_title"),
	YJGB_TXT("应急广播文本", "yjgb_txt"),
	YJGB_VIDEO("应急广播视频", "yjgb_video"),
	YJGB_AUDIO("应急广播音频", "yjgb_audio"),
	YJGB_PICTURE("应急广播图片", "yjgb_picture");
	
	private TemplateId(String name, String type){
		this.name = name;
		this.type = type;
	}
	
	private String name;
	
	private String type;

	public String getName() {
		return name;
	}

	public String getType() {
		return type;
	}
	
	public static TemplateId fromName(String name) throws Exception{
		TemplateId[] values = TemplateId.values();
		for(TemplateId value: values){
			if(value.getName().equals(name)){
				return value;
			}
		}
		throw new ErrorTypeException("name", name);
	}
	
	public static TemplateId fromType(String type) throws Exception{
		TemplateId[] values = TemplateId.values();
		for(TemplateId value: values){
			if(value.getType().equals(type)){
				return value;
			}
		}
		throw new ErrorTypeException("type", type);
	}
	
}
