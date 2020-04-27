package com.sumavision.bvc.command.group.enumeration;

import com.sumavision.tetris.orm.exception.ErrorTypeException;

/**
 * @ClassName: CommandGroupForwardDemandPO 指挥中转发点播的业务类型 
 * @author zsy
 * @date 2019年11月14日 上午10:11:22 
 */
public enum ForwardDemandBusinessType {
	
	FORWARD_DEVICE("转发设备", "设备"),
//	FORWARD_USER("转发设备", "用户"),
	FORWARD_FILE("转发文件", "文件");
//	FORWARD_RECORD("转发录像"),
//	PLAYER_CAST_DEVICE("播放器上屏");
	
	private String name;
	
	private String code;
	
	private ForwardDemandBusinessType(String name, String code){
		this.name = name;
		this.code = code;
	}

	public String getName(){
		return this.name;
	}

	public String getCode(){
		return this.code;
	}
	
	/**
	 * @Title: 根据名称获取转发目的类型 
	 * @param name 名称
	 * @throws Exception 
	 * @return ForwardDstType 转发目的类型
	 */
	public static ForwardDemandBusinessType fromName(String name) throws Exception{
		ForwardDemandBusinessType[] values = ForwardDemandBusinessType.values();
		for(ForwardDemandBusinessType value:values){
			if(value.getName().equals(name)){
				return value;
			}
		}
		throw new ErrorTypeException("name", name);
	}
	
}
