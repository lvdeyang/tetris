package com.sumavision.tetris.cs.channel.broad.terminal.requestBO;

import java.util.List;

public class RequestTerminalBO {
	/** tar包文件名 */
	private String file;
	
	/** tar包大小，单位B */
	private String fileSize;
	
	/** 发布版本号 */
	private String version;
	
	/** 整体目录描述 */
	private List<RequestTerminalDirBO> dir;
	
	/** 排期单描述 */
	private List<RequestTerminalScheduleBO> schedules;

	public String getFile() {
		return file;
	}

	public RequestTerminalBO setFile(String file) {
		this.file = file;
		return this;
	}

	public String getFileSize() {
		return fileSize;
	}

	public RequestTerminalBO setFileSize(String fileSize) {
		this.fileSize = fileSize;
		return this;
	}

	public String getVersion() {
		return version;
	}

	public RequestTerminalBO setVersion(String version) {
		this.version = version;
		return this;
	}

	public List<RequestTerminalDirBO> getDir() {
		return dir;
	}

	public RequestTerminalBO setDir(List<RequestTerminalDirBO> dir) {
		this.dir = dir;
		return this;
	}

	public List<RequestTerminalScheduleBO> getSchedules() {
		return schedules;
	}

	public RequestTerminalBO setSchedules(List<RequestTerminalScheduleBO> schedules) {
		this.schedules = schedules;
		return this;
	}
}
