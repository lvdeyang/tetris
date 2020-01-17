package com.sumavision.tetris.omms.hardware.backup;

import com.sumavision.tetris.orm.exception.ErrorTypeException;

/**
 * 备份类型<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2020年1月13日 下午1:10:38
 */
public enum BackupType {

	SERVER("服务器备份"),
	SERVICE("服务备份"),
	CLASS("类备份"),
	FILE("文件备份"),
	SQL("sql备份");
	
	private String name;
	
	public String getName(){
		return this.name;
	}
	
	private BackupType(String name){
		this.name = name;
	}

	public static BackupType fromName(String name) throws Exception{
		BackupType[] values = BackupType.values();
		for(BackupType value:values){
			if(value.getName().equals(name)){
				return value;
			}
		}
		throw new ErrorTypeException("name", name);
	}
	
}
