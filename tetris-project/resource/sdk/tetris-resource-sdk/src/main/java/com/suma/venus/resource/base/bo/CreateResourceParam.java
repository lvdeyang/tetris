package com.suma.venus.resource.base.bo;

import com.alibaba.fastjson.JSONObject;

public class CreateResourceParam {

	private Long userId;
	
	private JSONObject resource;

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public JSONObject getResource() {
		return resource;
	}

	public void setResource(JSONObject resource) {
		this.resource = resource;
	}
	
}
