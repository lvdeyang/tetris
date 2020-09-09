package com.suma.venus.resource.base.bo;

import com.alibaba.fastjson.JSONObject;

public class UpdateResourceParam {

	private Long userId;
	
	private String resourceId;
	
	private JSONObject attrs;

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getResourceId() {
		return resourceId;
	}

	public void setResourceId(String resourceId) {
		this.resourceId = resourceId;
	}

	public JSONObject getAttrs() {
		return attrs;
	}

	public void setAttrs(JSONObject attrs) {
		this.attrs = attrs;
	}
	
}
