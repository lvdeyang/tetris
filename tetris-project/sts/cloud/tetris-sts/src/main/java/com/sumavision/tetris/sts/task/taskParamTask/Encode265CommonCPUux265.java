package com.sumavision.tetris.sts.task.taskParamTask;

import java.io.Serializable;

public class Encode265CommonCPUux265 implements Encode265Common, Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5587376997565268800L;
	private String pixel_format;
	private Integer max_cu_size;
	private Integer max_tu_size;
	private Integer tu_depth;
	private Integer cu_depth;
	private Integer frame_num_threads;
	public String getPixel_format() {
		return pixel_format;
	}
	public void setPixel_format(String pixel_format) {
		this.pixel_format = pixel_format;
	}
	public Integer getMax_cu_size() {
		return max_cu_size;
	}
	public void setMax_cu_size(Integer max_cu_size) {
		this.max_cu_size = max_cu_size;
	}
	public Integer getMax_tu_size() {
		return max_tu_size;
	}
	public void setMax_tu_size(Integer max_tu_size) {
		this.max_tu_size = max_tu_size;
	}
	public Integer getTu_depth() {
		return tu_depth;
	}
	public void setTu_depth(Integer tu_depth) {
		this.tu_depth = tu_depth;
	}
	public Integer getCu_depth() {
		return cu_depth;
	}
	public void setCu_depth(Integer cu_depth) {
		this.cu_depth = cu_depth;
	}
	public Integer getFrame_num_threads() {
		return frame_num_threads;
	}
	public void setFrame_num_threads(Integer frame_num_threads) {
		this.frame_num_threads = frame_num_threads;
	}
	

}
