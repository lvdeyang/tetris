package com.sumavision.tetris.cs.channel;

import com.sumavision.tetris.orm.exception.ErrorTypeException;

public enum ChannelBroadStatus {
	CHANNEL_BROAD_STATUS_INIT("未播发", "10"),
	CHANNEL_BROAD_STATUS_BROADING("发送中", "0"),
	CHANNEL_BROAD_STATUS_BROADED("发送完成", "1"),
	CHANNEL_BROAD_STATUS_STOPPED("发送停止", "2");
	
	private String name;
	
	private String statusNum;
	
	private ChannelBroadStatus(String name, String statusNum) {
		this.name = name;
		this.statusNum = statusNum;
	}
	
	public String getName() {
		return name;
	}

	public String getStatusNum() {
		return statusNum;
	}

	public static ChannelBroadStatus fromName(String name) throws Exception{
		ChannelBroadStatus[] values = ChannelBroadStatus.values();
		
		for (ChannelBroadStatus channelBroadStatus : values) {
			if (channelBroadStatus.getName().equals(name)) {
				return channelBroadStatus;
			}
		}
		
		throw new ErrorTypeException("broadStatus", name);
	}
	
	public static String fromStatusNum(String statusNum) throws Exception{
		ChannelBroadStatus[] values = ChannelBroadStatus.values();
		
		for (ChannelBroadStatus channelBroadStatus : values) {
			if (channelBroadStatus.getStatusNum().equals(statusNum)) {
				return channelBroadStatus.getName();
			}
		}
		
		return "";
	}
}
