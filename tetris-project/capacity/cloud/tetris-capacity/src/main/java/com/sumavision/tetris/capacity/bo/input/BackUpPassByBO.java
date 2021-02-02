package com.sumavision.tetris.capacity.bo.input;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sumavision.tetris.application.template.SourceVO;
import com.sumavision.tetris.business.common.MissionBO;

import java.util.ArrayList;
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

	public BackUpPassByBO() {
	}

	public BackUpPassByBO(MissionBO missionBO, JSONObject sourceObj) {
		if (!sourceObj.containsKey("mode")) {
			this.mode = "higher_first";
		}else{
			this.mode = sourceObj.getString("mode");
		}
		if (!sourceObj.containsKey("select_index")) {
			this.select_index = "0";
		}else {
            Integer selectIdx = sourceObj.getInteger("select_index")-1;
            this.select_index = selectIdx.toString();
		}
		if (sourceObj.containsKey("trigger_list")) {
			this.trigger_list = JSON.parseObject(sourceObj.getString("trigger_list"),TriggerListBO.class) ;
		}
		if (sourceObj.containsKey("program_array")) {
			List<BackUpProgramBO> program_array = JSON.parseArray(sourceObj.getString("program_array"), BackUpProgramBO.class);
			this.setProgram_array(program_array);
		}else {
			List<BackUpProgramBO> program_array = new ArrayList<>();
			for (Integer index : missionBO.getInputMap().keySet()) {
				InputBO inputBO = missionBO.getInputMap().get(index);
				program_array.add(new BackUpProgramBO().setInput_id(inputBO.getId()));
			}
			this.setProgram_array(program_array);
		}



	}
}
