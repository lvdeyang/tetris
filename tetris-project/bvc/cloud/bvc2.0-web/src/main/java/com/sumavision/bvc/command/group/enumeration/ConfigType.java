package com.sumavision.bvc.command.group.enumeration;

import com.sumavision.tetris.orm.exception.ErrorTypeException;

/**
 * @ClassName: 配置类型 
 * @author zsy
 * @date 2019年9月23日 上午10:11:22 
 */
public enum ConfigType {

	BASIC_COMMAND_DEFAULT("普通指挥默认"),//所有成员看主席
	COOPERATE_COMMAND_AGENDA("协同指挥议程"),//主席与发言人1互相看
	SECRET_COMMAND_AGENDA("专向指挥议程"),//主席与发言人1互相看
	DEMAND_FORWARD("指挥转发点播"),
	
	AUDIO_CAST("语音广播"),
	AUDIO_CALL("语音通话"),
	
	MEETING_DEFAULT("会议默认"),//所有人看主席
	MEETING_AGENDA("发言"),//所有成员看发言人1，发言人1看主席
	MEETING_ROOM_FORWARD("会场转发");
	
	private String name;
	
	private ConfigType(String name){
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
	public static ConfigType fromName(String name) throws Exception{
		ConfigType[] values = ConfigType.values();
		for(ConfigType value:values){
			if(value.getName().equals(name)){
				return value;
			}
		}
		throw new ErrorTypeException("name", name);
	}
}
