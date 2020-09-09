package com.sumavision.tetris.capacity.bo.task;

public class FuzzyObjectBO {

	private String position;
	
	private String zone;
	
	/** 模糊效果 fuzzy/mosaic */
	private String fuzzy_effect;
	
	private Integer mosaic_radius;
	
	public String getPosition() {
		return position;
	}

	public FuzzyObjectBO setPosition(String position) {
		this.position = position;
		return this;
	}

	public String getZone() {
		return zone;
	}

	public FuzzyObjectBO setZone(String zone) {
		this.zone = zone;
		return this;
	}

	public String getFuzzy_effect() {
		return fuzzy_effect;
	}

	public FuzzyObjectBO setFuzzy_effect(String fuzzy_effect) {
		this.fuzzy_effect = fuzzy_effect;
		return this;
	}

	public Integer getMosaic_radius() {
		return mosaic_radius;
	}

	public FuzzyObjectBO setMosaic_radius(Integer mosaic_radius) {
		this.mosaic_radius = mosaic_radius;
		return this;
	}
	
}
