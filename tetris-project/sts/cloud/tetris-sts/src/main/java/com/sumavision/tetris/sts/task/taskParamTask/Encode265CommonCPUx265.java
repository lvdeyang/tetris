package com.sumavision.tetris.sts.task.taskParamTask;

import java.io.Serializable;

public class Encode265CommonCPUx265 implements Encode265Common, Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7484648503263945402L;
	private String preset;
	private String refine;
	private String pixel_format;
	private String bframe_reference;
	public Encode265CommonCPUx265(){
		super(); 
	}
	
	public Encode265CommonCPUx265(String preset, String refine,
			String pixel_format, String bframe_reference) {
		super();
		this.preset = preset;
		this.refine = refine;
		this.pixel_format = pixel_format;
		this.bframe_reference = bframe_reference.toLowerCase();
	}
	public String getPreset() {
		return preset;
	}
	public void setPreset(String preset) {
		this.preset = preset;
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
	
	

}
