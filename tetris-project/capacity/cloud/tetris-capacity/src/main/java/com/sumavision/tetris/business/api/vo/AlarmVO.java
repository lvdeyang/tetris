package com.sumavision.tetris.business.api.vo;

import com.alibaba.fastjson.JSONObject;

/**
 * 告警参数<br/>
 * <b>作者:</b>wjw<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2020年1月13日 下午5:13:29
 */
public class AlarmVO {

	private String codec;
	
	private String status;
	
	private String details;
	
	private JSONObject license_trigger;
	
	private InputTriggerVO input_trigger;
	
	private TaskTriggerVO task_trigger;
	
	private OutputTriggerVO output_trigger;

	public String getCodec() {
		return codec;
	}

	public void setCodec(String codec) {
		this.codec = codec;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}

	public JSONObject getLicense_trigger() {
		return license_trigger;
	}

	public void setLicense_trigger(JSONObject license_trigger) {
		this.license_trigger = license_trigger;
	}

	public InputTriggerVO getInput_trigger() {
		return input_trigger;
	}

	public void setInput_trigger(InputTriggerVO input_trigger) {
		this.input_trigger = input_trigger;
	}

	public TaskTriggerVO getTask_trigger() {
		return task_trigger;
	}

	public void setTask_trigger(TaskTriggerVO task_trigger) {
		this.task_trigger = task_trigger;
	}

	public OutputTriggerVO getOutput_trigger() {
		return output_trigger;
	}

	public void setOutput_trigger(OutputTriggerVO output_trigger) {
		this.output_trigger = output_trigger;
	}
	
}
