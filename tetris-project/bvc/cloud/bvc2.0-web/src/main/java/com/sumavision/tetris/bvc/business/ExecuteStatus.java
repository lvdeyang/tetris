package com.sumavision.tetris.bvc.business;

import com.sumavision.tetris.orm.exception.ErrorTypeException;

/**
 * 业务的执行状态<br/>
 * <p>完成、未执行</p>
 * <b>作者:</b>zsy<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2020年6月8日 上午11:51:04
 */
public enum ExecuteStatus {
	
	//停会时都处于UNDONE；开会之后UNDONE可能是成员未连接、尚未执行、因其它业务而暂停
	//呼叫等待接听时处于UNDONE
	DONE("完成"),
	UNDONE("未执行");

	private String name;
	
	private ExecuteStatus(String name){
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public static ExecuteStatus fromName(String name) throws Exception{
		ExecuteStatus[] values = ExecuteStatus.values();
		for(ExecuteStatus value:values){
			if(value.getName().equals(name)){
				return value;
			}
		}
		throw new ErrorTypeException("name", name);
	}
}
