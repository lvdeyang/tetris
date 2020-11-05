package com.suma.venus.resource.bo.mq;

import com.suma.venus.resource.base.bo.ResponseBody;

public class UpdateResourceResponse {

	private ResponseBody update_resource_response;
	
	public UpdateResourceResponse() {
	}

	public UpdateResourceResponse(ResponseBody update_resource_response) {
		this.update_resource_response = update_resource_response;
	}

	public ResponseBody getUpdate_resource_response() {
		return update_resource_response;
	}

	public void setUpdate_resource_response(ResponseBody update_resource_response) {
		this.update_resource_response = update_resource_response;
	}
	
}
