package com.sumavision.bvc.command.group.enumeration;

import com.sumavision.tetris.orm.exception.ErrorTypeException;

/**
 * @ClassName: CommandGroupForwardPO 转发的业务类型 
 * @author zsy
 * @date 2019年9月23日 上午10:11:22 
 */
public enum ForwardBusinessType {
	
	BASIC_COMMAND("普通会议业务"),//包括专向会议的转发，因为专项会议复用了普通会议
	COOPERATE_COMMAND("协同会议业务"),
	DEMAND_FORWARD("会议转发点播"),
	PLAYER_CAST_DEVICE("播放器上屏");
	
	private String name;
	
	private ForwardBusinessType(String name){
		this.name = name;
	}

	public String getName(){
		return this.name;
	}
	
	/**
	 * @Title: 根据名称获取转发目的类型 
	 * @param name 名称
	 * @throws Exception 
	 * @return ForwardDstType 转发目的类型
	 */
	public static ForwardBusinessType fromName(String name) throws Exception{
		ForwardBusinessType[] values = ForwardBusinessType.values();
		for(ForwardBusinessType value:values){
			if(value.getName().equals(name)){
				return value;
			}
		}
		throw new ErrorTypeException("name", name);
	}
	
}
