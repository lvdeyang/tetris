package com.sumavision.tetris.sts.task.taskParamInput;

import java.io.Serializable;

public class InputProgramVideoBO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6400655590204506057L;
	private Integer pid;
    private String type;
    private Integer width;
    private Integer height;
    private Integer fps;
    private Long bitrate;
    private String decode_mode;
    private Integer nv_card_idx;
    
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

	public Integer getWidth() {
		return width;
	}

	public void setWidth(Integer width) {
		this.width = width;
	}

	public Integer getHeight() {
		return height;
	}

	public void setHeight(Integer height) {
		this.height = height;
	}

	public Integer getFps() {
		return fps;
	}

	public void setFps(Integer fps) {
		this.fps = fps;
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

	public Integer getNv_card_idx() {
		return nv_card_idx;
	}

	public void setNv_card_idx(Integer nv_card_idx) {
		this.nv_card_idx = nv_card_idx;
	}

}
