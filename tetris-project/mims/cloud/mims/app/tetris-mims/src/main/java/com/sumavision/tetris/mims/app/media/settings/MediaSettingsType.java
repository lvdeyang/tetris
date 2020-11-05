package com.sumavision.tetris.mims.app.media.settings;

import com.sumavision.tetris.orm.exception.ErrorTypeException;

public enum MediaSettingsType {

	SWITCH_MEDIA_UPLOAD_REVIEW("媒资审核开关"),
	
	PROCESS_UPLOAD_PICTURE("图片上传流程"),
	PROCESS_EDIT_PICTURE("图片修改流程"),
	PROCESS_DELETE_PICTURE("图片删除流程"),
	
	PROCESS_UPLOAD_VIDEO("视频上传流程"),
	PROCESS_EDIT_VIDEO("视频修改流程"),
	PROCESS_DELETE_VIDEO("视频删除流程"),
	
	PROCESS_UPLOAD_AUDIO("音频上传流程"),
	PROCESS_EDIT_AUDIO("音频修改流程"),
	PROCESS_DELETE_AUDIO("音频删除流程"),
	
	PROCESS_UPLOAD_VIDEO_STREAM("视频流上传流程"),
	PROCESS_EDIT_VIDEO_STREAM("视频流修改流程"),
	PROCESS_DELETE_VIDEO_STREAM("视频流删除流程"),
	
	PROCESS_UPLOAD_AUDIO_STREAM("音频流上传流程"),
	PROCESS_EDIT_AUDIO_STREAM("音频流修改流程"),
	PROCESS_DELETE_AUDIO_STREAM("音频流删除流程"),
	
	PROCESS_UPLOAD_PUSH_LIVE("push直播上传流程"),
	PROCESS_EDIT_PUSH_LIVE("push直播修改流程"),
	PROCESS_DELETE_PUSH_LIVE("push直播删除流程"),
	
	PROCESS_UPLOAD_TXT("文本上传流程"),
	PROCESS_EDIT_TXT("文本修改流程"),
	PROCESS_DELETE_TXT("文本删除流程"),
	
	PROCESS_UPLOAD_COMPRESS("播发媒资上传流程"),
	PROCESS_EDIT_COMPRESS("播发媒资修改流程"),
	PROCESS_DELETE_COMPRESS("播发媒资删除流程");
	
	private String name;
	
	private MediaSettingsType(String name){
		this.name = name;
	}
	
	public String getName(){
		return this.name;
	}
	
	public static MediaSettingsType fromName(String name) throws Exception{
		MediaSettingsType[] values = MediaSettingsType.values();
		for(MediaSettingsType value:values){
			if(value.getName().equals(name)){
				return value;
			}
		}
		throw new ErrorTypeException("name", name);
	}
	
}
