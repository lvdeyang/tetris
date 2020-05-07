package com.sumavision.tetris.easy.process.stream.transcode;

public class StreamTranscodeProfileVO {
	private String toolIp;
	
	private String udpStartPort;

	public String getToolIp() {
		return toolIp;
	}

	public StreamTranscodeProfileVO setToolIp(String toolIp) {
		this.toolIp = toolIp;
		return this;
	}

	public String getUdpStartPort() {
		return udpStartPort;
	}

	public StreamTranscodeProfileVO setUdpStartPort(String udpStartPort) {
		this.udpStartPort = udpStartPort;
		return this;
	}
}
