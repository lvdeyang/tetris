package com.sumavision.tetris.business.record.vo;

/**
 *收录参数<br/>
 * <b>作者:</b>wjw<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2019年12月2日 上午11:36:22
 */
public class RecordVO {
	
	private String deviceIp;

	/** udp/rtp/http/hls/rtsp/rtmp */
	private String type;
	
	private SourceParamVO sourceParam;
	
	private OutputParamVO outputParam;

	public String getDeviceIp() {
		return deviceIp;
	}

	public RecordVO setDeviceIp(String deviceIp) {
		this.deviceIp = deviceIp;
		return this;
	}

	public String getType() {
		return type;
	}

	public RecordVO setType(String type) {
		this.type = type;
		return this;
	}

	public SourceParamVO getSourceParam() {
		return sourceParam;
	}

	public RecordVO setSourceParam(SourceParamVO sourceParam) {
		this.sourceParam = sourceParam;
		return this;
	}

	public OutputParamVO getOutputParam() {
		return outputParam;
	}

	public RecordVO setOutputParam(OutputParamVO outputParam) {
		this.outputParam = outputParam;
		return this;
	}
	
}
