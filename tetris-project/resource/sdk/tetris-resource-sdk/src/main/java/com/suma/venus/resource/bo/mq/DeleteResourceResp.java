package com.suma.venus.resource.bo.mq;

import com.suma.venus.resource.base.bo.ResponseBody;

public class DeleteResourceResp{

	private ResponseBody delete_resource_response;
	
	public DeleteResourceResp() {
	}

	public DeleteResourceResp(ResponseBody delete_resource_response) {
		this.delete_resource_response = delete_resource_response;
	}

	public ResponseBody getDelete_resource_response() {
		return delete_resource_response;
	}

	public void setDelete_resource_response(ResponseBody delete_resource_response) {
		this.delete_resource_response = delete_resource_response;
	}
	
}
