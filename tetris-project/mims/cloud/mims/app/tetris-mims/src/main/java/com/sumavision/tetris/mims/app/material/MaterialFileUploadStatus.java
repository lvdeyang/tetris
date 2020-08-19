package com.sumavision.tetris.mims.app.material;

import com.sumavision.tetris.orm.exception.ErrorTypeException;

/**
 * 素材上传状态<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2018年11月29日 上午11:36:54
 */
public enum MaterialFileUploadStatus {

	UPLOADING("上传中"),
	ERROR("上传出错"),
	COMPLETE("上传成功");
	
	private String name;
	
	private MaterialFileUploadStatus(String name){
		this.name = name;
	}
	
	public String getName(){
		return this.name;
	}
	
	public static MaterialFileUploadStatus fromName(String name) throws Exception{
		MaterialFileUploadStatus[] values = MaterialFileUploadStatus.values();
		for(MaterialFileUploadStatus value:values){
			if(value.getName().equals(name)){
				return value;
			}
		}
		throw new ErrorTypeException("name", name);
	}
	
}
