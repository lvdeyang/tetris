package com.suma.venus.resource.base.bo;

import com.alibaba.fastjson.JSONObject;

public class BaseChannelParamTemplateBody extends ResponseBody{

	private JSONObject base_param;
	
	public BaseChannelParamTemplateBody() {
	}
	
	public BaseChannelParamTemplateBody(JSONObject base_param) {
		this.base_param = base_param;
	}

	public BaseChannelParamTemplateBody(String result,JSONObject base_param) {
		super(result);
		this.base_param = base_param;
	}

	public JSONObject getBase_param() {
		return base_param;
	}

	public void setBase_param(JSONObject base_param) {
		this.base_param = base_param;
	}
	
}
