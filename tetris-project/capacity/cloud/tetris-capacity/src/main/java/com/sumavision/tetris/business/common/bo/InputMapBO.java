package com.sumavision.tetris.business.common.bo;

import com.sumavision.tetris.capacity.bo.input.InputBO;

/**
 * 输入PO和BO映射<br/>
 * <b>作者:</b>wjw<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2020年2月25日 下午4:49:31
 */
public class InputMapBO {

	private Long inputId;
	
	private InputBO inputBO;

	public Long getInputId() {
		return inputId;
	}

	public void setInputId(Long inputId) {
		this.inputId = inputId;
	}

	public InputBO getInputBO() {
		return inputBO;
	}

	public void setInputBO(InputBO inputBO) {
		this.inputBO = inputBO;
	}
	
}
