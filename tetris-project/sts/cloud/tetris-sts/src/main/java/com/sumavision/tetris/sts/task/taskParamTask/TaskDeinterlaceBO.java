package com.sumavision.tetris.sts.task.taskParamTask;

import java.io.Serializable;

public class TaskDeinterlaceBO implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 5841822507451879656L;
	
	private String mode;
	private String plat;
	private Integer nv_card_idx;
	public TaskDeinterlaceBO(String mode, String plat, Integer nv_card_idx){
		this.mode = mode;
		this.plat = plat;
		this.nv_card_idx = nv_card_idx;
	}
	public String getMode() {
		return mode;
	}
	public void setMode(String mode) {
		this.mode = mode;
	}
	public String getPlat() {
		return plat;
	}
	public void setPlat(String plat) {
		this.plat = plat;
	}
	public Integer getNv_card_idx() {
		return nv_card_idx;
	}
	public void setNv_card_idx(Integer nv_card_idx) {
		this.nv_card_idx = nv_card_idx;
	}
	
}
