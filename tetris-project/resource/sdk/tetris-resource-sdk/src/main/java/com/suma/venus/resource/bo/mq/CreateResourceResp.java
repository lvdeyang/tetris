package com.suma.venus.resource.bo.mq;

import com.suma.venus.resource.base.bo.ResponseBody;

public class CreateResourceResp {
	
	private ResponseBody create_resource_response;
	
	public CreateResourceResp() {
	}

	public CreateResourceResp(ResponseBody create_resource_response) {
		this.create_resource_response = create_resource_response;
	}

	public ResponseBody getCreate_resource_response() {
		return create_resource_response;
	}

	public void setCreate_resource_response(ResponseBody create_resource_response) {
		this.create_resource_response = create_resource_response;
	}
	
}
