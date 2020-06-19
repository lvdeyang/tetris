package com.sumavision.tetris.capacity.bo.task;

/**
 * 图像滤波<br/>
 * <b>作者:</b>wjw<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2020年2月12日 下午4:13:43
 */
public class ImageFilterBO {

	private String plat = "cpu";
	
	private Integer nv_card_idx;
	
	private String denoise;
	
	private String sharpen;

	public String getPlat() {
		return plat;
	}

	public ImageFilterBO setPlat(String plat) {
		this.plat = plat;
		return this;
	}

	public Integer getNv_card_idx() {
		return nv_card_idx;
	}

	public ImageFilterBO setNv_card_idx(Integer nv_card_idx) {
		this.nv_card_idx = nv_card_idx;
		return this;
	}

	
	public String getDenoise() {
		return denoise;
	}

	public ImageFilterBO setDenoise(String denoise) {
		this.denoise = denoise;
		return this;
	}

	public String getSharpen() {
		return sharpen;
	}

	public ImageFilterBO setSharpen(String sharpen) {
		this.sharpen = sharpen;
		return this;
	}
	
}
