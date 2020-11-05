package com.sumavision.tetris.business.common.bo;

import com.sumavision.tetris.capacity.bo.input.InputBO;

/**
 * 校验输入参数<br/>
 * <b>作者:</b>wjw<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2020年2月17日 上午11:21:44
 */
public class InputCheckBO {
	
	private boolean isExist;
	
	private InputBO inputBO;
	
	/** 持久化inputId */
	private Long inputId;

	public boolean isExist() {
		return isExist;
	}

	public void setExist(boolean isExist) {
		this.isExist = isExist;
	}

	public InputBO getInputBO() {
		return inputBO;
	}

	public void setInputBO(InputBO inputBO) {
		this.inputBO = inputBO;
	}

	public Long getInputId() {
		return inputId;
	}

	public void setInputId(Long inputId) {
		this.inputId = inputId;
	}
	
}
