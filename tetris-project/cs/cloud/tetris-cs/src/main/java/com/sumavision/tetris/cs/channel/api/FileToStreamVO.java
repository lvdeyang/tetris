package com.sumavision.tetris.cs.channel.api;

public class FileToStreamVO {
	/** 是否需要文件转流 */
	private boolean need;
	/** 文件地址 */
	private String fileUrl;
	/** 小工具ip */
	private String toolIp;
	/** 可用起始端口 */
	private String startPort;
	/** 文件播放次数 */
	private Integer playCount;
	/** 文件发流停止后回调 */
	private String stopCallback;
	
	public boolean isNeed() {
		return need;
	}

	public void setNeed(boolean need) {
		this.need = need;
	}

	public String getFileUrl() {
		return fileUrl;
	}
	
	public void setFileUrl(String fileUrl) {
		this.fileUrl = fileUrl;
	}
	
	public String getToolIp() {
		return toolIp;
	}

	public void setToolIp(String toolIp) {
		this.toolIp = toolIp;
	}

	public String getStartPort() {
		return startPort;
	}

	public void setStartPort(String startPort) {
		this.startPort = startPort;
	}

	public Integer getPlayCount() {
		return playCount;
	}
	
	public void setPlayCount(Integer playCount) {
		this.playCount = playCount;
	}
	
	public String getStopCallback() {
		return stopCallback;
	}
	
	public void setStopCallback(String stopCallback) {
		this.stopCallback = stopCallback;
	}
}
