 package com.sumavision.tetris.streamTranscodingProcessVO;

import java.util.List;

import com.sumavision.tetris.streamTranscoding.api.server.InputParamVO;
import com.sumavision.tetris.streamTranscoding.api.server.TaskVO;

public class StreamTranscodingVO {
	/** 是否转码 */
	private boolean isTranscoding;
	/** 是否添加收录输出 */
	private boolean needRecordOutput;
	/** 转码源地址 */
	private String assetUrl;
	/** 输入源为文件时使用参数 */
	private List<FileVO> files;
	/** 媒体类型 */
	private String mediaType;
	/** 节目号 */
	private Integer progNum;
	/** 音频源pcm */
	private Integer bePCM;
	/** 任务信息 */
	private TaskVO taskVO;
	/** 转换服务ip */
	private String deviceIp;
	/** 输入信息 */
	private InputParamVO inputParam;
	
	public boolean isTranscoding() {
		return isTranscoding;
	}
	
	public void setTranscoding(boolean isTranscoding) {
		this.isTranscoding = isTranscoding;
	}
	
	public boolean isNeedRecordOutput() {
		return needRecordOutput;
	}

	public void setNeedRecordOutput(boolean needRecordOutput) {
		this.needRecordOutput = needRecordOutput;
	}

	public String getAssetUrl() {
		return assetUrl;
	}

	public List<FileVO> getFiles() {
		return files;
	}

	public void setFiles(List<FileVO> files) {
		this.files = files;
	}

	public void setAssetUrl(String assetUrl) {
		this.assetUrl = assetUrl;
	}

	public Integer getBePCM() {
		return bePCM;
	}

	public void setBePCM(Integer bePCM) {
		this.bePCM = bePCM;
	}

	public String getMediaType() {
		return mediaType;
	}

	public void setMediaType(String mediaType) {
		this.mediaType = mediaType;
	}

	public Integer getProgNum() {
		return progNum;
	}

	public void setProgNum(Integer progNum) {
		this.progNum = progNum;
	}

	public TaskVO getTaskVO() {
		return taskVO;
	}

	public void setTaskVO(TaskVO taskVO) {
		this.taskVO = taskVO;
	}

	public String getDeviceIp() {
		return deviceIp;
	}

	public void setDeviceIp(String deviceIp) {
		this.deviceIp = deviceIp;
	}

	public InputParamVO getInputParam() {
		return inputParam;
	}

	public void setInputParam(InputParamVO inputParam) {
		this.inputParam = inputParam;
	}
}
