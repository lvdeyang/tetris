package com.sumavision.tetris.business.api.vo;

/**
 * 输入告警参数<br/>
 * <b>作者:</b>wjw<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2020年1月13日 下午5:17:51
 */
public class InputTriggerVO {

	private String input_id;
	
	private Integer program_id;
	
	private Integer element_id;

	public String getInput_id() {
		return input_id;
	}

	public void setInput_id(String input_id) {
		this.input_id = input_id;
	}

	public Integer getProgram_id() {
		return program_id;
	}

	public void setProgram_id(Integer program_id) {
		this.program_id = program_id;
	}

	public Integer getElement_id() {
		return element_id;
	}

	public void setElement_id(Integer element_id) {
		this.element_id = element_id;
	}
	
	
}
