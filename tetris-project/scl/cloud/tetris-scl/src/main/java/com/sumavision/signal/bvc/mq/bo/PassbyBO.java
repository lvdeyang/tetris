package com.sumavision.signal.bvc.mq.bo;

import com.alibaba.fastjson.JSONObject;

public class PassbyBO {

	private String layer_id;
	
	private String bundle_id;
	
	private String type;
	
	private JSONObject pass_by_content;

	public String getLayer_id() {
		return layer_id;
	}

	public void setLayer_id(String layer_id) {
		this.layer_id = layer_id;
	}

	public String getBundle_id() {
		return bundle_id;
	}

	public void setBundle_id(String bundle_id) {
		this.bundle_id = bundle_id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public JSONObject getPass_by_content() {
		return pass_by_content;
	}

	public void setPass_by_content(JSONObject pass_by_content) {
		this.pass_by_content = pass_by_content;
	}
	
}
