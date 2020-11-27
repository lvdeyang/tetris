package com.sumavision.tetris.capacity.bo.task;

import com.sumavision.tetris.application.template.*;

/**
 * 缩放参数<br/>
 * <b>作者:</b>wjw<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2019年10月29日 下午7:58:37
 */
public class ScaleBO {

	private Integer width;
	
	private Integer height;
	
	private String plat = "cpu";
	
	private String mode = "slow";
	
	private Integer nv_card_idx = 0;

	private Integer auto_blackside;

	private String ratio;

	public Integer getWidth() {
		return width;
	}

	public ScaleBO setWidth(Integer width) {
		this.width = width;
		return this;
	}

	public Integer getHeight() {
		return height;
	}

	public ScaleBO setHeight(Integer height) {
		this.height = height;
		return this;
	}

	public String getPlat() {
		return plat;
	}

	public ScaleBO setPlat(String plat) {
		this.plat = plat;
		return this;
	}

	public String getMode() {
		return mode;
	}

	public ScaleBO setMode(String mode) {
		this.mode = mode;
		return this;
	}

	public Integer getNv_card_idx() {
		return nv_card_idx;
	}

	public ScaleBO setNv_card_idx(Integer nv_card_idx) {
		this.nv_card_idx = nv_card_idx;
		return this;
	}

	public Integer getAuto_blackside() {
		return auto_blackside;
	}

	public ScaleBO setAuto_blackside(Integer auto_blackside) {
		this.auto_blackside = auto_blackside;
		return this;
	}

	public String getRatio() {
		return ratio;
	}

	public ScaleBO setRatio(String ratio) {
		this.ratio = ratio;
		return this;
	}

	public ScaleBO() {
	}

	public ScaleBO(ProcessVO processVO) {
		this.width = processVO.getWidth();
		this.height = processVO.getHeight();
		this.plat = processVO.getPlat().name().toLowerCase();
		this.mode = processVO.getMode().name().toLowerCase();
		this.nv_card_idx = processVO.getNv_card_idx();
		this.auto_blackside = processVO.getAuto_blackside();
		this.ratio = processVO.getRatio();
	}
}
