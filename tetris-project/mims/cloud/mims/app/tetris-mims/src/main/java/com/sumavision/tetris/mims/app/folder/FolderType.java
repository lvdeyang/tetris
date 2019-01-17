package com.sumavision.tetris.mims.app.folder;

import com.sumavision.tetris.orm.exception.ErrorTypeException;

/**
 * 文件夹类型<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2018年11月21日 上午11:42:45
 */
public enum FolderType {
	
	PERSONAL("私人文件夹"),
	COMPANY("企业文件夹"),
	COMPANY_PICTURE("图片库"),
	COMPANY_VIDEO("视频库"),
	COMPANY_AUDIO("音频库"),
	COMPANY_VIDEO_STREAM("视频流库"),
	COMPANY_AUDIO_STREAM("音频流库"),
	COMPANY_TXT("文本库"),
	SHARE("共享文件夹");
	
	private String name;
	
	private FolderType(String name){
		this.name = name;
	}
	
	public String getName(){
		return this.name;
	}
	
	public static FolderType fromName(String name) throws Exception{
		FolderType[] values = FolderType.values();
		for(FolderType value:values){
			if(value.getName().equals(name)){
				return value;
			}
		}
		throw new ErrorTypeException("name", name);
	}
}
