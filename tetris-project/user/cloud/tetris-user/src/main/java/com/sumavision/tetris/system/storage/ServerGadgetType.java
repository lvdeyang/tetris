package com.sumavision.tetris.system.storage;

import com.sumavision.tetris.orm.exception.ErrorTypeException;

/**
 * 服务器小工具类型<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2019年11月6日 上午11:35:10
 */
public enum ServerGadgetType {

	SUMAVISION_GADGET("数码视讯小工具", "com.sumavision.tetris.system.storage.gadget.SumavisionGadget");
	
	/** 小工具名称 */
	private String name;

	/** bean名称 */
	private String beanName;
	
	private ServerGadgetType(String name, String beanName){
		this.name = name;
		this.beanName = beanName;
	}
	
	public String getName(){
		return this.name;
	}
	
	public String getBeanName(){
		return this.beanName;
	}
	
	public static ServerGadgetType fromName(String name) throws Exception{
		ServerGadgetType[] values = ServerGadgetType.values();
		for(ServerGadgetType value:values){
			if(value.getName().equals(name)){
				return value;
			}
		}
		throw new ErrorTypeException("name", name);
	}
	
	public static ServerGadgetType fromBeanName(String beanName) throws Exception{
		ServerGadgetType[] values = ServerGadgetType.values();
		for(ServerGadgetType value:values){
			if(value.getBeanName().equals(beanName)){
				return value;
			}
		}
		throw new ErrorTypeException("beanName", beanName);
	}
	
}
