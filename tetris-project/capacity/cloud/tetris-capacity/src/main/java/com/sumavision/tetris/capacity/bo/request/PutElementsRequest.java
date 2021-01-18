package com.sumavision.tetris.capacity.bo.request;

import com.alibaba.fastjson.annotation.JSONField;
import com.sumavision.tetris.capacity.bo.input.InputBO;
import com.sumavision.tetris.capacity.bo.input.ModifyElementBO;

/**
 * 修改输入参数<br/>
 * <b>作者:</b>wjw<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2019年11月5日 上午10:04:50
 */
public class PutElementsRequest {

	private String msg_id;

	@JSONField(serialize = false)
	private String input_id;

	@JSONField(serialize = false)
	private Integer program_num;

	@JSONField(serialize = false)
	private Integer pid;

	private ModifyElementBO param;

	public String getMsg_id() {
		return msg_id;
	}

	public PutElementsRequest setMsg_id(String msg_id) {
		this.msg_id = msg_id;
		return this;
	}

	public ModifyElementBO getParam() {
		return param;
	}

	public void setParam(ModifyElementBO param) {
		this.param = param;
	}

	public String getInput_id() {
		return input_id;
	}

	public void setInput_id(String input_id) {
		this.input_id = input_id;
	}

	public Integer getProgram_num() {
		return program_num;
	}

	public PutElementsRequest setProgram_num(Integer program_num) {
		this.program_num = program_num;
		return this;
	}

	public Integer getPid() {
		return pid;
	}

	public PutElementsRequest setPid(Integer pid) {
		this.pid = pid;
		return this;
	}

	public PutElementsRequest() {
		// Do nothing because of X and Y.
	}
}
