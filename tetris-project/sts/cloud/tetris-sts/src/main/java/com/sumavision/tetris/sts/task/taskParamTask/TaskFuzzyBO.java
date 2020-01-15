package com.sumavision.tetris.sts.task.taskParamTask;

import java.io.Serializable;

public class TaskFuzzyBO implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -2451609705122918140L;
	
	private String position;
	private String zone;
	private String fuzzy_effect;
	private Integer mosaic_radius;
	
	public TaskFuzzyBO(String position, String zone, String fuzzy_effect, Integer mosaic_radius) {
		super();
		this.position = position;
		this.zone = zone;
		this.fuzzy_effect = fuzzy_effect;
		this.mosaic_radius = mosaic_radius;
	}
	public String getPosition() {
		return position;
	}
	public void setPosition(String position) {
		this.position = position;
	}
	public String getZone() {
		return zone;
	}
	public void setZone(String zone) {
		this.zone = zone;
	}
	public String getFuzzy_effect() {
		return fuzzy_effect;
	}
	public void setFuzzy_effect(String fuzzy_effect) {
		this.fuzzy_effect = fuzzy_effect;
	}
	public Integer getMosaic_radius() {
		return mosaic_radius;
	}
	public void setMosaic_radius(Integer mosaic_radius) {
		this.mosaic_radius = mosaic_radius;
	}
		
}
