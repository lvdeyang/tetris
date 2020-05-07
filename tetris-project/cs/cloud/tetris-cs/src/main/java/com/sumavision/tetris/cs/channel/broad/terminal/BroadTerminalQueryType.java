package com.sumavision.tetris.cs.channel.broad.terminal;

public enum BroadTerminalQueryType {
	START_SEND_FILE("DTMB播发", "/ed/speaker/startSendFile"),
	RESTART_SEND_FILE("重新播发", "/ed/speaker/startSendFile"),
	STOP_SEND_FILE("停止播发", "/ed/speaker/stopSendFile"),
	QUERY_SEND_FILE("状态查询", "/ed/speaker/querySendFile"),
	QUERY_DIVISION_TREE("地区查询", "/ed/ed/regiondivision/queryDivisionTree"),
	IP_PUSH_SEND("4G播发", "/ed/speaker/pushFtp"),
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

	public String getUri() {
		return uri;
	}
}
