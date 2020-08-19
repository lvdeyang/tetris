package com.sumavision.tetris.capacity.bo.input;

import java.util.List;

public class BackUpPassByBO {

	/** 模式 higher_first/floating/manual */
	private String mode;
	
	/** 选中索引 */
	private String select_index = "0";
	
	/** 输入id */
	private String input_id;
	
	/** 触发列表 */
	private TriggerListBO trigger_list;
	
	/** 备份节目数组 */
	private List<BackUpProgramBO> program_array;

	public String getMode() {
		return mode;
	}

	public BackUpPassByBO setMode(String mode) {
		this.mode = mode;
		return this;
	}

	public String getSelect_index() {
		return select_index;
	}

	public BackUpPassByBO setSelect_index(String select_index) {
		this.select_index = select_index;
		return this;
	}

	public String getInput_id() {
		return input_id;
	}

	public BackUpPassByBO setInput_id(String input_id) {
		this.input_id = input_id;
		return this;
	}

	public TriggerListBO getTrigger_list() {
		return trigger_list;
	}

	public BackUpPassByBO setTrigger_list(TriggerListBO trigger_list) {
		this.trigger_list = trigger_list;
		return this;
	}

	public List<BackUpProgramBO> getProgram_array() {
		return program_array;
	}

	public BackUpPassByBO setProgram_array(List<BackUpProgramBO> program_array) {
		this.program_array = program_array;
		return this;
	}
	
}
