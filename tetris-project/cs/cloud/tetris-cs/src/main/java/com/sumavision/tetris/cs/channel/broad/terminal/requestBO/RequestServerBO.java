package com.sumavision.tetris.cs.channel.broad.terminal.requestBO;

import java.util.List;

public class RequestServerBO {
	/** 播发标识 */
	private String id;
	
	/** 是否以tar包方式下任务 */
	private Integer hasFile;
	
	/** 播发等级 */
	private Integer level;
	
	/** 播发地区列表 */
	private List<String> regionList;
	
	/** 是否以DTMB播发 */
	private String fileType;
	
	/** tar包文件获取地址 */
	private String filePath;
	
	/** tar包文件大小 */
	private String fileSize;
	
	/** 不携带tar包的直播freq */
	private String freq;
	
	/** 不携带tar包的直播audioPid */
	private String audioPid;
	
	/** 不携带tar包的直播videoPid */
	private String videoPid;

	public String getId() {
		return id;
	}

	public RequestServerBO setId(String id) {
		this.id = id;
		return this;
	}

	public Integer getHasFile() {
		return hasFile;
	}

	public RequestServerBO setHasFile(Integer hasFile) {
		this.hasFile = hasFile;
		return this;
	}

	public Integer getLevel() {
		return level;
	}

	public RequestServerBO setLevel(Integer level) {
		this.level = level;
		return this;
	}

	public List<String> getRegionList() {
		return regionList;
	}

	public RequestServerBO setRegionList(List<String> regionList) {
		this.regionList = regionList;
		return this;
	}

	public String getFileType() {
		return fileType;
	}

	public RequestServerBO setFileType(String fileType) {
		this.fileType = fileType;
		return this;
	}

	public String getFilePath() {
		return filePath;
	}

	public RequestServerBO setFilePath(String filePath) {
		this.filePath = filePath;
		return this;
	}

	public String getFileSize() {
		return fileSize;
	}

	public RequestServerBO setFileSize(String fileSize) {
		this.fileSize = fileSize;
		return this;
	}

	public String getFreq() {
		return freq;
	}

	public RequestServerBO setFreq(String freq) {
		this.freq = freq;
		return this;
	}

	public String getAudioPid() {
		return audioPid;
	}

	public RequestServerBO setAudioPid(String audioPid) {
		this.audioPid = audioPid;
		return this;
	}

	public String getVideoPid() {
		return videoPid;
	}

	public RequestServerBO setVideoPid(String videoPid) {
		this.videoPid = videoPid;
		return this;
	}
}
