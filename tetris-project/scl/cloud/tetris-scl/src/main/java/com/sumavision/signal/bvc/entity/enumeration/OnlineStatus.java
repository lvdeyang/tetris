package com.sumavision.signal.bvc.entity.enumeration;

/**
 * 在线状态枚举<br/>
 * <b>作者:</b>wjw<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2019年8月1日 下午1:41:51
 */
public enum OnlineStatus {

	ONLINE("在线"),
	OFFLINE("离线");
	
	private OnlineStatus(String name){
		this.name = name;
	}
	
	private String name;

	public String getName() {
		return name;
	}
	
}
