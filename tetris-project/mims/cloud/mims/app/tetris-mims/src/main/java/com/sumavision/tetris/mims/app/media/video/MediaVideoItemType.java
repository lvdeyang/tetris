package com.sumavision.tetris.mims.app.media.video;

import com.sumavision.tetris.orm.exception.ErrorTypeException;

public enum MediaVideoItemType {

	FOLDER("文件夹", "icon-folder-close", new String[]{"font-size:20px; position:relative; top:2px; color:#FFD659;"}),
	VIDEO("视频", "icon-film", new String[]{"font-size:16px; position:relative; top:1px;"});
	
	/** 名称 */
	private String name;
	
	/** 图标 */
	private String icon;
	
	/** 图标微调 */
	private String[] style;
	
	private MediaVideoItemType(String name, String icon, String[] style){
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
	
	public static MediaVideoItemType fromName(String name) throws Exception{
		MediaVideoItemType[] values = MediaVideoItemType.values();
		for(MediaVideoItemType value:values){
			if(value.getName().equals(name)){
				return value;
			}
		}
		throw new ErrorTypeException("name", name);
	}
	
}
