package com.sumavision.tetris.cs.channel.broad.terminal.requestBO;

import java.util.List;

public class RequestTerminalDirBO {
	/** tar包内绝对路径，./标识根目录 */
	private String path;
	
	/** 标识目录还是文件 */
	private String type;
	
	/** 子项 */
	private List<RequestTerminalDirBO> subs;

	public String getPath() {
		return path;
	}

	public RequestTerminalDirBO setPath(String path) {
		this.path = path;
		return this;
	}

	public String getType() {
		return type;
	}

	public RequestTerminalDirBO setType(String type) {
		this.type = type;
		return this;
	}

	public List<RequestTerminalDirBO> getSubs() {
		return subs;
	}

	public RequestTerminalDirBO setSubs(List<RequestTerminalDirBO> subs) {
		this.subs = subs;
		return this;
	}
}
