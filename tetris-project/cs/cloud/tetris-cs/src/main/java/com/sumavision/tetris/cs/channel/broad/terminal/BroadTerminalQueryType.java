package com.sumavision.tetris.cs.channel.broad.terminal;

import com.sumavision.tetris.cs.channel.BroadWay;
import com.sumavision.tetris.cs.channel.ChannelBroadStatus;
import com.sumavision.tetris.cs.channel.exception.ChannelTerminalRequestErrorException;

public enum BroadTerminalQueryType {
	START_SEND_FILE("开始播发", "/ed/speaker/startSendFile"),
	RESTART_SEND_FILE("重新播发", "/ed/speaker/startSendFile"),
	STOP_SEND_FILE("停止播发", "/ed/speaker/stopSendFile"),
	QUERY_SEND_FILE("状态查询", "/ed/speaker/querySendFile"),
	QUERY_DIVISION_TREE("地区查询", "/ed/ed/regiondivision/queryDivisionTree"),
	RESET_ZONE_URL("重置分片地址", "");
	
	private String action;
	private String uri;
	
	private BroadTerminalQueryType(String action, String uri) {
		this.action = action;
		this.uri = uri;
	}

	public String getAction() {
		return action;
	}
	
	public String getUrl() throws Exception{
		String url = ChannelBroadStatus.getBroadcastIPAndPort(BroadWay.TERMINAL_BROAD);
		if (!url.isEmpty()) {
			return "http://" + url + uri;
		}else {
			throw new ChannelTerminalRequestErrorException(action, "请求url配置信息出错");
		}
	}
}
