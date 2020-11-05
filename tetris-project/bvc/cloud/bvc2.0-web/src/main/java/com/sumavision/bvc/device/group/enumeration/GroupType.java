package com.sumavision.bvc.device.group.enumeration;

import com.sumavision.tetris.orm.exception.ErrorTypeException;

/**
 * @ClassName: 设备组类型 <br/>
 * @Description: 业务上分为监控业务和会议业务<br/>
 * @author lvdeyang
 * @date 2018年7月31日 下午1:41:24 
 */
public enum GroupType {

	MEETING("会议室", "1", true),
	MONITOR("监控室", "2", true),
	MULTIPLAYERVIDEO("多人视频通话", "3", false),
	MULTIPLAYERAUDIO("多人语音通话", "3", false);
//	MEETING_PUBLISH("会议发布直播", "5", false);//protocalId作为逻辑层录制命令的videoType，5表示发布直播
	
	private String name;
	
	private String protocalId;
	
	private boolean show;
	
	private GroupType(String name, String protocalId, boolean show){
		this.name = name;
		this.protocalId = protocalId;
		this.show = show;
	}
	
	public String getName(){
		return this.name;
	}
	
	public String getProtocalId(){
		return this.protocalId;
	}
	
	public boolean getShow(){
		return this.show;
	}
	
	/**
	 * @Title: 根据名称获取组类型 
	 * @param name
	 * @throws Exception 
	 * @return GroupType 设备组类型 
	 */
	public static GroupType fromName(String name) throws Exception{
		GroupType[] values = GroupType.values();
		for(GroupType value:values){
			if(value.getName().equals(name)){
				return value;
			}
		}
		throw new ErrorTypeException("name", name);
	}
	
}
