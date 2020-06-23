package com.sumavision.tetris.capacity.bo.request;

/**
 * 修改指定节目解码配置<br/>
 * <b>作者:</b>wjw<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2019年11月5日 下午1:22:22
 */
public class PatchDecodeRequest {

	private String msg_id;

	private String input_id;

	private String program_num;

	private String pid;

	private String decode_mode;

	public String getMsg_id() {
		return msg_id;
	}

	public PatchDecodeRequest setMsg_id(String msg_id) {
		this.msg_id = msg_id;
		return this;
	}

	public String getDecode_mode() {
		return decode_mode;
	}

	public PatchDecodeRequest setDecode_mode(String decode_mode) {
		this.decode_mode = decode_mode;
		return this;
	}

	public String getInput_id() {
		return input_id;
	}

	public void setInput_id(String input_id) {
		this.input_id = input_id;
	}

	public String getProgram_num() {
		return program_num;
	}

	public void setProgram_num(String program_num) {
		this.program_num = program_num;
	}

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}
}
