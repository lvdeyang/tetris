package com.sumavision.tetris.sts.task.taskParamTask;

import java.io.Serializable;

public class Encode264CommonGPUIntelBO implements Encode264Common, Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3612896865503803363L;
	private Boolean bframe_adaptive;
	private Boolean bframe_reference;
	private Integer refine;
	private Integer max_bframe;
	private Integer quality;
	public Boolean getBframe_adaptive() {
		return bframe_adaptive;
	}
	public void setBframe_adaptive(Boolean bframe_adaptive) {
		this.bframe_adaptive = bframe_adaptive;
	}
	public Boolean getBframe_reference() {
		return bframe_reference;
	}
	public void setBframe_reference(Boolean bframe_reference) {
		this.bframe_reference = bframe_reference;
	}
	public Integer getRefine() {
		return refine;
	}
	public void setRefine(Integer refine) {
		this.refine = refine;
	}
	public Integer getMax_bframe() {
		return max_bframe;
	}
	public void setMax_bframe(Integer max_bframe) {
		this.max_bframe = max_bframe;
	}
	public Integer getQuality() {
		return quality;
	}
	public void setQuality(Integer quality) {
		this.quality = quality;
	}
	
	
	
	

}
