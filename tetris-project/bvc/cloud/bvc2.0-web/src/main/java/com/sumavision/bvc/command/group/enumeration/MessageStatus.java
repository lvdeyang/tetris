package com.sumavision.bvc.command.group.enumeration;

import com.sumavision.tetris.orm.exception.ErrorTypeException;

/**
 * @ClassName: 消息状态
 * @author zsy
 * @date 2019年11月19日 上午10:11:22 
 */
public enum MessageStatus {

	START("正在通知"),
	STOP("通知结束");
	
	private String name;
	
	private MessageStatus(String name){
		this.name = name;
	}
	
	public String getName(){
		return this.name;
	}
	
	/**
	 * @Title: 根据名称获取会议配置类型 
	 * @param name 名称
	 * @throws Exception 
	 * @return ConfigType 会议配置类型
	 */
	public static MessageStatus fromName(String name) throws Exception{
		MessageStatus[] values = MessageStatus.values();
		for(MessageStatus value:values){
			if(value.getName().equals(name)){
				return value;
			}
		}
		throw new ErrorTypeException("name", name);
	}
}
