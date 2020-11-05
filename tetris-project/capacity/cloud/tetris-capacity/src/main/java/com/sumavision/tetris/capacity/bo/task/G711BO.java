package com.sumavision.tetris.capacity.bo.task;

/**
 * G711参数<br/>
 * <b>作者:</b>wjw<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2019年11月1日 上午10:09:43
 */
public class G711BO extends BaseAudioEncodeBO<G711BO>{
	
	/** 10-50 */
	private Integer ptime = 20;

	public Integer getPtime() {
		return ptime;
	}

	public G711BO setPtime(Integer ptime) {
		this.ptime = ptime;
		return this;
	}
	
}
