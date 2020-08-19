package com.sumavision.signal.bvc.network.bo;

public class CreateOutputResponseBO {

	private Integer code;
	
	private String msg;
	
	private DataBO data;

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public DataBO getData() {
		return data;
	}

	public void setData(DataBO data) {
		this.data = data;
	}
	
}
