//package com.sumavision.bvc.command.group.enumeration;
//
//import com.sumavision.tetris.orm.exception.ErrorTypeException;
//
//public enum AgendaForwardBusinessType {
//	
//	COMMAND_FORWARD("会议转发", "媒体转发", "commandForward", true),
////	COMMAND_FORWARD_DEVICE("会议转发设备", "媒体转发", "commandForwardDevice", true),
////	COMMAND_FORWARD_FILE("会议转发文件", "媒体转发", "commandForwardFile", true),
////	COMMAND_FORWARD_USER("会议转发用户", "媒体转发", "commandForwardUser", true);
//	CROSS_COMMAND("越级指挥业务", "越级指挥", "crossCommand", true),
//	SECRET_COMMAND("专向会议业务", "专向指挥", "secretCommand", true),
//	COOPERATESERVICE_COMMAND("协同指挥业务", "协同指挥", "cooperateCommand", true);
//	
//	private String name;
//	
//	 /** 用于展现在播放器上的业务类型信息 */
//	private String info;
//	
//	private String code;
//	
//	private boolean show;
//	
//	private AgendaForwardBusinessType(String name, String info, String code, boolean show){
//		this.name = name;
//		this.info = info;
//		this.code = code;
//		this.show = show;
//	}
//
//	public String getName(){
//		return this.name;
//	}
//
//	public String getInfo(){
//		return this.info;
//	}
//	
//	public String getCode() {
//		return code;
//	}
//	
//	public boolean isShow() {
//		return show;
//	}
//	
//	/**
//	 * @Title: 根据名称获取转发类型 
//	 * @param name 名称
//	 */
//	public static AgendaForwardBusinessType fromName(String name) throws Exception{
//		AgendaForwardBusinessType[] values = AgendaForwardBusinessType.values();
//		for(AgendaForwardBusinessType value:values){
//			if(value.getName().equals(name)){
//				return value;
//			}
//		}
//		throw new ErrorTypeException("name", name);
//	}
//}
