package com.sumavision.tetris.capacity.bo.task;

/**
 * 音柱参数<br/>
 * <b>作者:</b>wjw<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2019年10月29日 下午7:58:37
 */
public class AudioColumnBO {

	private String plat = "cpu";
	
	private String playflag = "on";

	public String getPlat() {
		return plat;
	}

	public AudioColumnBO setPlat(String plat) {
		this.plat = plat;
		return this;
	}

	public String getPlayflag() {
		return playflag;
	}

	public AudioColumnBO setPlayflag(String playflag) {
		this.playflag = playflag;
		return this;
	}
}
