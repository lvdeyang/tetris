package com.sumavision.tetris.capacity.bo.request;

import java.util.List;

import com.sumavision.tetris.capacity.bo.task.DecodePreProcessingBO;
import com.sumavision.tetris.capacity.bo.task.PreProcessingBO;

/**
 * 修改任务解码后处理参数<br/>
 * <b>作者:</b>wjw<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2019年11月6日 上午9:09:46
 */
public class PutTaskDecodeProcessRequest {

	private String msg_id;

	private String task_id;

	private List<DecodePreProcessingBO> process_array;

	public String getMsg_id() {
		return msg_id;
	}

	public PutTaskDecodeProcessRequest setMsg_id(String msg_id) {
		this.msg_id = msg_id;
		return this;
	}

	public List<DecodePreProcessingBO> getProcess_array() {
		return process_array;
	}

	public void setProcess_array(List<DecodePreProcessingBO> process_array) {
		this.process_array = process_array;
	}

	public String getTask_id() {
		return task_id;
	}

	public void setTask_id(String task_id) {
		this.task_id = task_id;
	}
}
