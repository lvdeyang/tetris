package com.sumavision.tetris.mims.app.media.audio;

import com.sumavision.tetris.orm.exception.ErrorTypeException;

public enum MediaAudioItemType {

	FOLDER("文件夹", "icon-folder-close", new String[]{"font-size:20px; position:relative; top:2px; color:#FFD659;"}),
	AUDIO("音频", "icon-music", new String[]{"font-size:16px; position:relative; top:1px;"});
	
	/** 名称 */
	private String name;
	
	/** 图标 */
	private String icon;
	
	/** 图标微调 */
	private String[] style;
	
	private MediaAudioItemType(String name, String icon, String[] style){
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
	
	public static MediaAudioItemType fromName(String name) throws Exception{
		MediaAudioItemType[] values = MediaAudioItemType.values();
		for(MediaAudioItemType value:values){
			if(value.getName().equals(name)){
				return value;
			}
		}
		throw new ErrorTypeException("name", name);
	}
	
}
