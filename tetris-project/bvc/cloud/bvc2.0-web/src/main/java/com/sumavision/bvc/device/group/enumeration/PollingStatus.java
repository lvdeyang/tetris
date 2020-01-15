package com.sumavision.bvc.device.group.enumeration;

import com.sumavision.tetris.orm.exception.ErrorTypeException;

/**
 * @ClassName: 描述合屏分屏的轮询状态
 * @author lvdeyang
 * @date 2018年7月31日 下午8:23:28 
 */
public enum PollingStatus {

	RUN("轮询中", "polling"),
	PAUSE("暂停", "pause");
	
	private String name;
	
	private String protocal;
	
	private PollingStatus(String name, String protocal){
		this.name = name;
		this.protocal = protocal;
	}
	
	public String getName(){
		return this.name;
	}
	
	public String getProtocal(){
		return this.protocal;
	}
	
	/**
	 * @Title: 根据名称获取合屏分屏的轮询状态 <br/>
	 * @param name 
	 * @return PollingStatus 合屏分屏的轮询状态
	 */
	public static PollingStatus fromName(String name) throws Exception{
		PollingStatus[] values = PollingStatus.values();
		for(PollingStatus value:values){
			if(value.getName().equals(name)){
				return value;
			}
		}
		throw new ErrorTypeException("name", name);
	}

}
