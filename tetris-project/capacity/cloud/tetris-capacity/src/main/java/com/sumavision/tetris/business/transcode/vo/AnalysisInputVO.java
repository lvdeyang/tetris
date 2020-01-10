package com.sumavision.tetris.business.transcode.vo;

import com.sumavision.tetris.capacity.bo.input.InputBO;

/**
 * 刷源信息<br/>
 * <b>作者:</b>wjw<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2020年1月2日 下午4:49:34
 */
public class AnalysisInputVO {

	/** 能力ip */
	private String device_ip;
	
	/** 输入信息 */
	private InputBO input;

	public String getDevice_ip() {
		return device_ip;
	}

	public void setDevice_ip(String device_ip) {
		this.device_ip = device_ip;
	}

	public InputBO getInput() {
		return input;
	}

	public void setInput(InputBO input) {
		this.input = input;
	}
	
}
