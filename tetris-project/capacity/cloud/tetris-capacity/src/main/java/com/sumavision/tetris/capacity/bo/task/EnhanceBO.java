package com.sumavision.tetris.capacity.bo.task;

import com.sumavision.tetris.application.template.ProcessVO;

/**
 * 图像增强参数<br/>
 * <b>作者:</b>wjw<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2019年10月30日 上午8:54:35
 */
public class EnhanceBO {
	
	private String plat = "cpu";
	
	private Integer nv_card_idx;

	/** 亮度 -255-255 */
	private Integer brightness;
	
	/** 色度 */
	private Integer chrome;
	
	/** 对比度 -100-100 */
	private Integer contrast;
	
	/** 饱和度 -100-100 */
	private Integer saturation;
	
	/** Gamma变换 */
	private Integer gamma;

	private Integer x;

	private Integer y;

	private Integer width;

	private Integer height;

	private String localenhance;

	public String getPlat() {
		return plat;
	}

	public EnhanceBO setPlat(String plat) {
		this.plat = plat;
		return this;
	}

	public Integer getNv_card_idx() {
		return nv_card_idx;
	}

	public EnhanceBO setNv_card_idx(Integer nv_card_idx) {
		this.nv_card_idx = nv_card_idx;
		return this;
	}

	public Integer getBrightness() {
		return brightness;
	}

	public EnhanceBO setBrightness(Integer brightness) {
		this.brightness = brightness;
		return this;
	}

	public Integer getContrast() {
		return contrast;
	}

	public EnhanceBO setContrast(Integer contrast) {
		this.contrast = contrast;
		return this;
	}

	public Integer getSaturation() {
		return saturation;
	}

	public EnhanceBO setSaturation(Integer saturation) {
		this.saturation = saturation;
		return this;
	}

	public Integer getChrome() {
		return chrome;
	}

	public EnhanceBO setChrome(Integer chrome) {
		this.chrome = chrome;
		return this;
	}

	public Integer getGamma() {
		return gamma;
	}

	public EnhanceBO setGamma(Integer gamma) {
		this.gamma = gamma;
		return this;
	}

	public Integer getX() {
		return x;
	}

	public EnhanceBO setX(Integer x) {
		this.x = x;
		return this;
	}

	public Integer getY() {
		return y;
	}

	public EnhanceBO setY(Integer y) {
		this.y = y;
		return this;
	}

	public Integer getWidth() {
		return width;
	}

	public EnhanceBO setWidth(Integer width) {
		this.width = width;
		return this;
	}

	public Integer getHeight() {
		return height;
	}

	public EnhanceBO setHeight(Integer height) {
		this.height = height;
		return this;
	}

	public String getLocalenhance() {
		return localenhance;
	}

	public void setLocalenhance(String localenhance) {
		this.localenhance = localenhance;
	}

	public EnhanceBO() {
	}

	public EnhanceBO(ProcessVO processVO) {
		this.plat = processVO.getPlat().name().toLowerCase();
		this.nv_card_idx = processVO.getNv_card_idx();
		this.brightness = processVO.getBrightness();
		this.chrome = processVO.getChrome();
		this.contrast = processVO.getContrast();
		this.saturation = processVO.getSaturation();
		this.gamma = processVO.getGamma();
		this.x = processVO.getX();
		this.y = processVO.getY();
		this.width = processVO.getWidth();
		this.height = processVO.getHeight();
		this.localenhance = processVO.getLocalenhance();
	}
}
