package com.sumavision.tetris.media.editor.task;

import com.sumavision.tetris.orm.exception.ErrorTypeException;

/**
 * 媒体编辑任务状态<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2019年4月2日 上午10:21:58
 */
public enum MediaEditorTaskStatus {

	APPROVING("审核中"),
	APPROVED("已批准"),
	UNAPPROVED("未批准");
	
	private String name;
	
	private MediaEditorTaskStatus(String name){
		this.name = name;
	}
	
	public String getName(){
		return this.name;
	}
	
	public static MediaEditorTaskStatus fromName(String name) throws Exception{
		MediaEditorTaskStatus[] values = MediaEditorTaskStatus.values();
		for(MediaEditorTaskStatus value:values){
			if(value.getName().equals(name)){
				return value;
			}
		}
		throw new ErrorTypeException("name", name);
	}
	
}
