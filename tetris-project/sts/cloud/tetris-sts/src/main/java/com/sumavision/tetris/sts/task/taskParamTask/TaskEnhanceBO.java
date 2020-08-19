package com.sumavision.tetris.sts.task.taskParamTask;

import java.io.Serializable;

public class TaskEnhanceBO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3051156897095767873L;
	
	private Integer brightness;
	private Integer contrast;
	private Integer saturation;
	private String denosie;
	private String sharpen;
	private String colorspace;
	private String colortransfer;
	private String colorprimaries;
	private String colorrange;
	
	public TaskEnhanceBO(Integer brightness, Integer contrast,
			Integer saturation, String denosie, String sharpen,
			String colorspace, String colortransfer, String colorprimaries,
			String colorrange) {
		super();
		this.brightness = brightness;
		this.contrast = contrast;
		this.saturation = saturation;
		this.denosie = denosie;
		this.sharpen = sharpen;
		this.colorspace = colorspace;
		this.colortransfer = colortransfer;
		this.colorprimaries = colorprimaries;
		this.colorrange = colorrange;
	}
	public Integer getBrightness() {
		return brightness;
	}
	public void setBrightness(Integer brightness) {
		this.brightness = brightness;
	}
	public Integer getContrast() {
		return contrast;
	}
	public void setContrast(Integer contrast) {
		this.contrast = contrast;
	}
	public Integer getSaturation() {
		return saturation;
	}
	public void setSaturation(Integer saturation) {
		this.saturation = saturation;
	}
	public String getDenosie() {
		return denosie;
	}
	public void setDenosie(String denosie) {
		this.denosie = denosie;
	}
	public String getSharpen() {
		return sharpen;
	}
	public void setSharpen(String sharpen) {
		this.sharpen = sharpen;
	}
	public String getColorspace() {
		return colorspace;
	}
	public void setColorspace(String colorspace) {
		this.colorspace = colorspace;
	}
	public String getColortransfer() {
		return colortransfer;
	}
	public void setColortransfer(String colortransfer) {
		this.colortransfer = colortransfer;
	}
	public String getColorprimaries() {
		return colorprimaries;
	}
	public void setColorprimaries(String colorprimaries) {
		this.colorprimaries = colorprimaries;
	}
	public String getColorrange() {
		return colorrange;
	}
	public void setColorrange(String colorrange) {
		this.colorrange = colorrange;
	}
	
	

}
