package com.suma.venus.resource.base.bo;

import com.alibaba.fastjson.JSONObject;

public class ExternChannelParamTemplateBody extends ResponseBody{

	private JSONObject extern_param;
	
	public ExternChannelParamTemplateBody() {
	}
	
	public ExternChannelParamTemplateBody(JSONObject extern_param) {
		this.extern_param = extern_param;
	}

	public ExternChannelParamTemplateBody(String result,JSONObject extern_param) {
		super(result);
		this.extern_param = extern_param;
	}

	public JSONObject getExtern_param() {
		return extern_param;
	}

	public void setExtern_param(JSONObject extern_param) {
		this.extern_param = extern_param;
	}
	
}
