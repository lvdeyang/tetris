package com.sumavision.tetris.capacity.bo.input;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sumavision.tetris.business.common.MissionBO;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 备份es和raw的参数<br/>
 * <b>作者:</b>wjw<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2019年10月29日 下午2:58:51
 */
public class BackUpEsAndRawBO {

	/** 模式 higher_first/floating/manual */
	private String mode = "higher_first";
	
	/** 选中索引 */
	private String select_index;
	
	/** 触发列表 */
	private TriggerListBO trigger_list;
	
	/** 备份节目数组 */
	private List<BackUpProgramBO> program_array;
	
	private ProgramOutputBO output_program;

	private JSONObject options;
	
	private BackUpFileBO file;

	public String getMode() {
		return mode;
	}

	public BackUpEsAndRawBO setMode(String mode) {
		this.mode = mode;
		return this;
	}

	public String getSelect_index() {
		return select_index;
	}

	public BackUpEsAndRawBO setSelect_index(String select_index) {
		this.select_index = select_index;
		return this;
	}

	public List<BackUpProgramBO> getProgram_array() {
		return program_array;
	}

	public BackUpEsAndRawBO setProgram_array(List<BackUpProgramBO> program_array) {
		this.program_array = program_array;
		return this;
	}

	public ProgramOutputBO getOutput_program() {
		return output_program;
	}

	public BackUpEsAndRawBO setOutput_program(ProgramOutputBO output_program) {
		this.output_program = output_program;
		return this;
	}

	public BackUpFileBO getFile() {
		return file;
	}

	public BackUpEsAndRawBO setFile(BackUpFileBO file) {
		this.file = file;
		return this;
	}

	public TriggerListBO getTrigger_list() {
		return trigger_list;
	}

	public BackUpEsAndRawBO setTrigger_list(TriggerListBO trigger_list) {
		this.trigger_list = trigger_list;
		return this;
	}

	public JSONObject getOptions() {
		return options;
	}

	public void setOptions(JSONObject options) {
		this.options = options;
	}

	public BackUpEsAndRawBO() {
	}

	public BackUpEsAndRawBO(MissionBO missionBO, JSONObject sourceObj) {
		if (!sourceObj.containsKey("mode")) {
			this.mode = "higher_first";
		}else{
			this.mode = sourceObj.getString("mode");
		}

		if (sourceObj.containsKey("trigger_list")) {
			this.trigger_list = JSON.parseObject(sourceObj.getString("trigger_list"),TriggerListBO.class) ;
		}
		if (sourceObj.containsKey("i_frame_switch")) {
			JSONObject opt = new JSONObject();
			opt.put("i_frame_switch",sourceObj.getBooleanValue("i_frame_switch"));
			this.options = opt;
		}else if (sourceObj.containsKey("options")){
			this.options = sourceObj.getJSONObject("options");
		}


		Integer selectIdx=0;
		if (sourceObj.containsKey("select_index")) {
			selectIdx = sourceObj.getInteger("select_index");
		}
		List<BackUpProgramBO> program_array = new ArrayList<>();
		for (InputBO inputBO : missionBO.getInputMap().values()) {
			BackUpProgramBO backUpProgramBO = null;
			if (inputBO.getProgram_array().size()>1 && sourceObj.containsKey("select_program_number")) {
				ProgramBO programBO = inputBO.getProgram_array().stream().filter(p -> sourceObj.getInteger("select_program_number").equals(p.getProgram_number())).findFirst().get();
				backUpProgramBO = new BackUpProgramBO(inputBO.getId(),programBO);
			}else{
				ProgramBO programBO = inputBO.getProgram_array().get(0);
				backUpProgramBO = new BackUpProgramBO(inputBO.getId(),programBO);
			}
			program_array.add(backUpProgramBO);
		}
		int i=0;
		for (Integer index : missionBO.getInputMap().keySet()) {
			if (selectIdx.equals(index)) {
				selectIdx=i;
				break;
			}
			i++;
		}
		this.setProgram_array(program_array);
		this.select_index=selectIdx.toString();

		ProgramBO programBO = missionBO.getInputMap().values().stream().findFirst().get().getProgram_array().get(0);
		ProgramOutputBO programOutputBO = new ProgramOutputBO(programBO);
		this.output_program = programOutputBO;
	}
}
