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
	COMPANY_PICTURE("图片库", "picture", "picture"),
	COMPANY_VIDEO("视频库", "video", "video"),
	COMPANY_AUDIO("音频库", "audio", "audio"),
	COMPANY_VIDEO_STREAM("视频流库", "videoStream", "video-stream"),
	COMPANY_AUDIO_STREAM("音频流库", "audioStream", "audio-stream"),
	COMPANY_PUSH_LIVE("push直播库", "pushLive", "push-live"),
	COMPANY_TXT("文本库", "txt", "txt"),
	COMPANY_COMPRESS("压缩包库", "compress", "compress"),
	SHARE("共享文件夹");
	
	private String name;
	
	private String primaryKey;
	
	private String webSuffix;
	
	private FolderType(String name){
		this.name = name;
	}
	
	private FolderType(String name, String primaryKey, String webSuffix){
		this.name = name;
		this.primaryKey = primaryKey;
		this.webSuffix = webSuffix;
	}
	
	public String getName(){
		return this.name;
	}
	
	public String getPrimaryKey(){
		return this.primaryKey;
	}
	
	public String getWebSuffix(){
		return this.webSuffix;
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
	
	public static FolderType fromPrimaryKey(String primaryKey) throws Exception{
		FolderType[] values = FolderType.values();
		for(FolderType value:values){
			if(value.getPrimaryKey()!=null && value.getPrimaryKey().equals(primaryKey)){
				return value;
			}
		}
		throw new ErrorTypeException("primaryKey", primaryKey);
	}
	
}
