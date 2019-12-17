package com.sumavision.tetris.sts.task.taskParamTask;

import java.io.Serializable;

public class TaskScaleBO implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8070325231507476918L;
	
	private Integer width;
	private Integer height;
	private String plat;
	private String mode;
	private String ratio;
	private Integer nv_card_idx;
	
	public TaskScaleBO(Integer width, Integer height, String plat, String mode,
			String ratio, Integer nv_card_idx) {
		super();
		this.width = width;
		this.height = height;
		this.plat = plat;
		this.mode = mode;
		this.ratio = ratio;
		this.nv_card_idx = nv_card_idx;
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
	public String getPlat() {
		return plat;
	}
	public void setPlat(String plat) {
		this.plat = plat;
	}
	public String getMode() {
		return mode;
	}
	public void setMode(String mode) {
		this.mode = mode;
	}
	public String getRatio() {
		return ratio;
	}
	public void setRatio(String ratio) {
		this.ratio = ratio;
	}
	public Integer getNv_card_idx() {
		return nv_card_idx;
	}
	public void setNv_card_idx(Integer nv_card_idx) {
		this.nv_card_idx = nv_card_idx;
	}
	
	
}
