package com.sumavision.tetris.data.warehouse;

import com.alibaba.fastjson.JSONArray;

public class DataWarehouseResponseArrayVO {
	/** 状态码 */
	private String statcode;
	/** 错误信息 */
	private String err;
	/** 返回内容 */
	private JSONArray data;
	
	public String getStatcode() {
		return statcode;
	}
	
	public void setStatcode(String statcode) {
		this.statcode = statcode;
	}
	
	public String getErr() {
		return err;
	}
	
	public void setErr(String err) {
		this.err = err;
	}
	
	public JSONArray getData() {
		return data;
	}
	
	public void setData(JSONArray data) {
		this.data = data;
	}
}
