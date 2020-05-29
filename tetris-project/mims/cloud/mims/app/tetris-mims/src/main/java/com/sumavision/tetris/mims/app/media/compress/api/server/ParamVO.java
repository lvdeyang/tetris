package com.sumavision.tetris.mims.app.media.compress.api.server;

/**
 * paser接口中param相关参数<br/>
 * <b>作者:</b>wjw<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2020年5月13日 下午4:47:53
 */
public class ParamVO {

	/** 频点 */
	private String freq;
	
	/** 视频pid */
	private String vpid;
	
	/** 音频pid */
	private String apid;
	
	/** 返参时候加的参数 */
	/** 严重级别 */
	private String severity;

	public String getFreq() {
		return freq;
	}

	public void setFreq(String freq) {
		this.freq = freq;
	}

	public String getVpid() {
		return vpid;
	}

	public void setVpid(String vpid) {
		this.vpid = vpid;
	}

	public String getApid() {
		return apid;
	}

	public void setApid(String apid) {
		this.apid = apid;
	}

	public String getSeverity() {
		return severity;
	}

	public void setSeverity(String severity) {
		this.severity = severity;
	}
	
}
