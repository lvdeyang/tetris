package com.sumavision.tetris.business.director.bo;

import com.sumavision.tetris.capacity.bo.input.InputBO;

/**
 * 校验InputBO是否需要创建<br/>
 * <b>作者:</b>wjw<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2020年2月14日 下午4:23:32
 */
public class InputCheckBO {

	private boolean isExsit;
	
	private InputBO input;

	public boolean isExsit() {
		return isExsit;
	}

	public void setExsit(boolean isExsit) {
		this.isExsit = isExsit;
	}

	public InputBO getInput() {
		return input;
	}

	public void setInput(InputBO input) {
		this.input = input;
	}
	
}
