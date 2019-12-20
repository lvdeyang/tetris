package com.sumavision.tetris.sts.task.taskParamTask;

import java.io.Serializable;

public class Encode264CommonCPUx264BO implements Encode264Common, Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1864634693360287203L;
	private Integer keyint_min;
	private Integer keyint_max;
	private String refine;
	private String pixel_format;
	private String bframe_reference;
	private Boolean bframe_adaptive;
	private Integer me_range;
	private Boolean low_latency;
	public Encode264CommonCPUx264BO(){
		super();
	}
	
	public Encode264CommonCPUx264BO(Integer keyint_min, Integer keyint_max,
			String refine, String pixel_format, String bframe_reference,
			Boolean bframe_adaptive, Integer me_range, Boolean low_latency) {
		super();
		this.keyint_min = keyint_min;
		this.keyint_max = keyint_max;
		this.refine = refine;
		this.pixel_format = pixel_format;
		this.bframe_reference = bframe_reference.toLowerCase();
		this.bframe_adaptive = bframe_adaptive;
		this.me_range = me_range;
		this.low_latency = low_latency;
	}
	public Integer getKeyint_min() {
		return keyint_min;
	}
	public void setKeyint_min(Integer keyint_min) {
		this.keyint_min = keyint_min;
	}
	public Integer getKeyint_max() {
		return keyint_max;
	}
	public void setKeyint_max(Integer keyint_max) {
		this.keyint_max = keyint_max;
	}
	public String getRefine() {
		return refine;
	}
	public void setRefine(String refine) {
		this.refine = refine;
	}
	public String getPixel_format() {
		return pixel_format;
	}
	public void setPixel_format(String pixel_format) {
		this.pixel_format = pixel_format;
	}
	public String getBframe_reference() {
		return bframe_reference.toLowerCase();
	}
	public void setBframe_reference(String bframe_reference) {
		this.bframe_reference = bframe_reference.toLowerCase();
	}
	public Boolean getBframe_adaptive() {
		return bframe_adaptive;
	}
	public void setBframe_adaptive(Boolean bframe_adaptive) {
		this.bframe_adaptive = bframe_adaptive;
	}
	public Integer getMe_range() {
		return me_range;
	}
	public void setMe_range(Integer me_range) {
		this.me_range = me_range;
	}
	public Boolean getLow_latency() {
		return low_latency;
	}
	public void setLow_latency(Boolean low_latency) {
		this.low_latency = low_latency;
	}
	

}
