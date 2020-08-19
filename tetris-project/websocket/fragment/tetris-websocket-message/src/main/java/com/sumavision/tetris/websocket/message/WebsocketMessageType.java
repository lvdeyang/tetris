package com.sumavision.tetris.websocket.message;

/**
 * 推送消息类型<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2019年9月11日 上午11:08:04
 */
public enum WebsocketMessageType {

	INSTANT_MESSAGE("即时消息"),
	COMMAND("命令");
	
	private String name;
	
	private WebsocketMessageType(String name){
		this.name = name;
	}
	
	public String getName(){
		return this.name;
	}
	
	public static WebsocketMessageType fromName(String name){
		WebsocketMessageType[] values = WebsocketMessageType.values();
		for(WebsocketMessageType value:values){
			if(value.getName().equals(name)){
				return value;
			}
		}
		return null;
	}
	
}
