package com.sumavision.tetris.capacity.bo.request;

import java.util.List;

/**
 * 删除任务编码请求参数<br/>
 * <b>作者:</b>wjw<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2019年11月5日 下午4:38:12
 */
public class DeleteTaskEncodeResponse {

	private String msg_id;

	private String task_id;

	private List<IdRequest> encode_array;

	public String getMsg_id() {
		return msg_id;
	}

	public DeleteTaskEncodeResponse setMsg_id(String msg_id) {
		this.msg_id = msg_id;
		return this;
	}

	public List<IdRequest> getEncode_array() {
		return encode_array;
	}

	public DeleteTaskEncodeResponse setEncode_array(List<IdRequest> encode_array) {
		this.encode_array = encode_array;
		return this;
	}

	public String getTask_id() {
		return task_id;
	}

	public void setTask_id(String task_id) {
		this.task_id = task_id;
	}
}
