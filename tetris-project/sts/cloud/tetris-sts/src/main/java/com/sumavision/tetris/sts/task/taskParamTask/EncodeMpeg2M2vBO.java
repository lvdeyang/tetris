package com.sumavision.tetris.sts.task.taskParamTask;

import java.io.Serializable;

public class EncodeMpeg2M2vBO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3620698807100238713L;
	private Boolean ts_control;
	private Boolean scenecut;
	private Integer keyint_max;
	public EncodeMpeg2M2vBO(){
		super();
	}
	
	public EncodeMpeg2M2vBO(Boolean ts_control, Boolean scenecut,Integer keyint_max) {
		super();
		this.ts_control = ts_control;
		this.scenecut = scenecut;
		this.keyint_max = keyint_max;
	}
	public Boolean getTs_control() {
		return ts_control;
	}
	public void setTs_control(Boolean ts_control) {
		this.ts_control = ts_control;
	}
	public Boolean getScenecut() {
		return scenecut;
	}
	public void setScenecut(Boolean scenecut) {
		this.scenecut = scenecut;
	}
	public Integer getKeyint_max() {
		return keyint_max;
	}
	public void setKeyint_max(Integer keyint_max) {
		this.keyint_max = keyint_max;
	}
	

}
