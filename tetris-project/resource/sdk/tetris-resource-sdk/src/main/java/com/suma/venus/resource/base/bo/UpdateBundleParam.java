package com.suma.venus.resource.base.bo;

import com.alibaba.fastjson.JSONObject;

public class UpdateBundleParam {

	private String bundle_id;
	
	private JSONObject attrs;

	public String getBundle_id() {
		return bundle_id;
	}

	public void setBundle_id(String bundle_id) {
		this.bundle_id = bundle_id;
	}

	public JSONObject getAttrs() {
		return attrs;
	}

	public void setAttrs(JSONObject attrs) {
		this.attrs = attrs;
	}
	
}
