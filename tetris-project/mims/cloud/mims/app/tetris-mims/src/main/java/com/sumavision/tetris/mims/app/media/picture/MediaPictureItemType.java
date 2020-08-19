package com.sumavision.tetris.mims.app.media.picture;

import com.sumavision.tetris.orm.exception.ErrorTypeException;

public enum MediaPictureItemType {

	FOLDER("文件夹", "icon-folder-close", new String[]{"font-size:20px; position:relative; top:2px; color:#FFD659;"}),
	PICTURE("图片", "icon-picture", new String[]{"font-size:16px; position:relative; top:1px;"});
	
	/** 名称 */
	private String name;
	
	/** 图标 */
	private String icon;
	
	/** 图标微调 */
	private String[] style;
	
	private MediaPictureItemType(String name, String icon, String[] style){
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
	
	public static MediaPictureItemType fromName(String name) throws Exception{
		MediaPictureItemType[] values = MediaPictureItemType.values();
		for(MediaPictureItemType value:values){
			if(value.getName().equals(name)){
				return value;
			}
		}
		throw new ErrorTypeException("name", name);
	}
	
}
