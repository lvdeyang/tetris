package com.sumavision.tetris.mims.app.media;

import com.sumavision.tetris.orm.exception.ErrorTypeException;

/**
 * 媒资上传状态<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2019年1月28日 上午10:17:18
 */
public enum UploadStatus {

	UPLOADING("上传中"),
	ERROR("上传出错"),
	COMPLETE("上传成功");
	
	private String name;
	
	private UploadStatus(String name){
		this.name = name;
	}
	
	public String getName(){
		return this.name;
	}
	
	public static UploadStatus fromName(String name) throws Exception{
		UploadStatus[] values = UploadStatus.values();
		for(UploadStatus value:values){
			if(value.getName().equals(name)){
				return value;
			}
		}
		throw new ErrorTypeException("name", name);
	}
	
}
