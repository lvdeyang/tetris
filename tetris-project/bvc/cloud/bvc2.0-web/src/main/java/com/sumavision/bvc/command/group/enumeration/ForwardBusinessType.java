package com.sumavision.bvc.command.group.enumeration;

import com.sumavision.tetris.orm.exception.ErrorTypeException;

/**
 * @ClassName: CommandGroupForwardPO 转发的业务类型 
 * @author zsy
 * @date 2019年9月23日 上午10:11:22 
 */
public enum ForwardBusinessType {
	
	BASIC_COMMAND("普通指挥业务"),//包括专向指挥的转发，因为专项指挥复用了普通指挥
	COOPERATE_COMMAND("协同指挥业务"),
	DEMAND_FORWARD("指挥转发点播"),
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
