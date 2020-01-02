package com.sumavision.tetris.data.warehouse;

import com.alibaba.fastjson.JSONObject;

/**
 * 数据仓库请求体
 * @author lzp
 *
 */
public class DataWarehouseRequestVO {
	/** 请求动作 */
	private String action;
	
	/** 请求内容 */
	private JSONObject data;
	
	public DataWarehouseRequestVO(String action) {
		this.action = action;
	}

	public String getAction() {
		return action;
	}

	public DataWarehouseRequestVO setAction(String action) {
		this.action = action;
		return this;
	}

	public JSONObject getData() {
		return data;
	}

	public DataWarehouseRequestVO setData(JSONObject data) {
		this.data = data;
		return this;
	}
}
