package com.sumavision.tetris.oldCMS.api.server;

public class StreamVO {
	private Boolean isTranscoding;
	private String assetPath;
	private String mediaType;
	private Integer audioType;
	private String audioParam;
	private Integer bePCM;
	private Integer esType;
	private Integer streamPubType;
	private String inputParam;
	private String sipParam;
	private String videoParam;
	private Long progNum;
	private String outputParam;
	private String dataParam;
	private Boolean record;
	private String recordCallback;
	private String localIp;
	
	public Boolean getIsTranscoding() {
		return isTranscoding;
	}
	public void setIsTranscoding(Boolean isTranscoding) {
		this.isTranscoding = isTranscoding;
	}
	public String getAssetPath() {
		return assetPath;
	}
	public void setAssetPath(String assetPath) {
		this.assetPath = assetPath;
	}
	public String getMediaType() {
		return mediaType;
	}
	public void setMediaType(String mediaType) {
		this.mediaType = mediaType;
	}
	public Integer getAudioType() {
		return audioType;
	}
	public void setAudioType(Integer audioType) {
		this.audioType = audioType;
	}
	public String getAudioParam() {
		return audioParam;
	}
	public void setAudioParam(String audioParam) {
		this.audioParam = audioParam;
	}
	public Integer getBePCM() {
		return bePCM;
	}
	public void setBePCM(Integer bePCM) {
		this.bePCM = bePCM;
	}
	public Integer getEsType() {
		return esType;
	}
	public void setEsType(Integer esType) {
		this.esType = esType;
	}
	public Integer getStreamPubType() {
		return streamPubType;
	}
	public void setStreamPubType(Integer streamPubType) {
		this.streamPubType = streamPubType;
	}
	public String getInputParam() {
		return inputParam;
	}
	public void setInputParam(String inputParam) {
		this.inputParam = inputParam;
	}
	public String getSipParam() {
		return sipParam;
	}
	public void setSipParam(String sipParam) {
		this.sipParam = sipParam;
	}
	public String getVideoParam() {
		return videoParam;
	}
	public void setVideoParam(String videoParam) {
		this.videoParam = videoParam;
	}
	public Long getProgNum() {
		return progNum;
	}
	public void setProgNum(Long progNum) {
		this.progNum = progNum;
	}
	public String getOutputParam() {
		return outputParam;
	}
	public void setOutputParam(String outputParam) {
		this.outputParam = outputParam;
	}
	public String getDataParam() {
		return dataParam;
	}
	public void setDataParam(String dataParam) {
		this.dataParam = dataParam;
	}
	public Boolean getRecord() {
		return record;
	}
	public void setRecord(Boolean record) {
		this.record = record;
	}
	public String getRecordCallback() {
		return recordCallback;
	}
	public void setRecordCallback(String recordCallback) {
		this.recordCallback = recordCallback;
	}
	public String getLocalIp() {
		return localIp;
	}
	public void setLocalIp(String localIp) {
		this.localIp = localIp;
	}
}
