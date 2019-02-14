package com.sumavision.tetris.mims.app.media.txt;

import com.sumavision.tetris.orm.exception.ErrorTypeException;

public enum MediaTxtItemType {

	FOLDER("文件夹", "icon-folder-close", new String[]{"font-size:20px; position:relative; top:2px; color:#FFD659;"}),
	TXT("文本", "el-icon-document", new String[]{"font-size:16px;"});
	
	/** 名称 */
	private String name;
	
	/** 图标 */
	private String icon;
	
	/** 图标微调 */
	private String[] style;
	
	private MediaTxtItemType(String name, String icon, String[] style){
		this.name = name;
		this.icon = icon;
		this.style = style;
	}
	
	public String getName(){
		return this.name;
	}
	
	public String getIcon(){
		return this.icon;
	}
	
	public String[] getStyle(){
		return this.style;
	}
	
	public static MediaTxtItemType fromName(String name) throws Exception{
		MediaTxtItemType[] values = MediaTxtItemType.values();
		for(MediaTxtItemType value:values){
			if(value.getName().equals(name)){
				return value;
			}
		}
		throw new ErrorTypeException("name", name);
	}
	
}
