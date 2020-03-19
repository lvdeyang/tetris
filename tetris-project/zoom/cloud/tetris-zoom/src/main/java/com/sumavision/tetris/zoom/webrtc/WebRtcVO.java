package com.sumavision.tetris.zoom.webrtc;

public class WebRtcVO {

	private String ip;
	
	private String layerId;
	
	private String httpPort;
	
	private String websocketPort;

	public String getIp() {
		return ip;
	}

	public WebRtcVO setIp(String ip) {
		this.ip = ip;
		return this;
	}

	public String getLayerId() {
		return layerId;
	}

	public WebRtcVO setLayerId(String layerId) {
		this.layerId = layerId;
		return this;
	}

	public String getHttpPort() {
		return httpPort;
	}

	public WebRtcVO setHttpPort(String httpPort) {
		this.httpPort = httpPort;
		return this;
	}

	public String getWebsocketPort() {
		return websocketPort;
	}

	public WebRtcVO setWebsocketPort(String websocketPort) {
		this.websocketPort = websocketPort;
		return this;
	}
	
}
