package com.sumavision.tetris.cs.channel;

public class SetTerminalBroadBO {
	/** 终端播发是否携带tar文件 */
	private Boolean hasFile;
	
	/** 终端播发等级 */
	private String level;

	public Boolean getHasFile() {
		return hasFile;
	}

	public SetTerminalBroadBO setHasFile(Boolean hasFile) {
		this.hasFile = hasFile;
		return this;
	}

	public String getLevel() {
		return level;
	}

	public SetTerminalBroadBO setLevel(String level) {
		this.level = level;
		return this;
	}
}
