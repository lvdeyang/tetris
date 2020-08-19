package com.sumavision.tetris.sts.task.taskParamInput;

import java.io.Serializable;

public class InputProgramAudioBO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7323796713292984379L;
	private Integer pid;
    private String type;
    private Long bitrate;
    private String decode_mode;

    public Integer getPid() {
		return pid;
	}

	public void setPid(Integer pid) {
		this.pid = pid;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Long getBitrate() {
		return bitrate;
	}

	public void setBitrate(Long bitrate) {
		this.bitrate = bitrate;
	}

	public String getDecode_mode() {
		return decode_mode;
	}

	public void setDecode_mode(String decode_mode) {
		this.decode_mode = decode_mode;
	}

}
