package com.sumavision.tetris.capacity.bo.response;

/**
 * 获取输入源返回<br/>
 * <b>作者:</b>wjw<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2019年11月5日 下午2:20:49
 */
public class AnalysisResponse {

	private String msg_id;
	
	private InputResultResponse input;

	/**
	 * 刷源设备；
	 */
	private String device_ip;

	public String getMsg_id() {
		return msg_id;
	}

	public AnalysisResponse setMsg_id(String msg_id) {
		this.msg_id = msg_id;
		return this;
	}

	public InputResultResponse getInput() {
		return input;
	}

	public AnalysisResponse setInput(InputResultResponse input) {
		this.input = input;
		return this;
	}

	public String getDevice_ip() {
		return device_ip;
	}

	public AnalysisResponse setDevice_ip(String device_ip) {
		this.device_ip = device_ip;
		return this;
	}
}
