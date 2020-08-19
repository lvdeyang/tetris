package com.sumavision.tetris.business.api.vo;

/**
 * 任务告警参数<br/>
 * <b>作者:</b>wjw<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2020年1月13日 下午5:22:36
 */
public class TaskTriggerVO {

	private String task_id;
	
	private String encode_id;

	public String getTask_id() {
		return task_id;
	}

	public void setTask_id(String task_id) {
		this.task_id = task_id;
	}

	public String getEncode_id() {
		return encode_id;
	}

	public void setEncode_id(String encode_id) {
		this.encode_id = encode_id;
	}
	
}
