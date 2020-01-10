package com.suma.venus.resource.bo.mq;

import com.suma.venus.resource.base.bo.ResponseBody;

public class LayerOnlineCertifyResp{

	private ResponseBody layer_online_certify_response;
	
	public LayerOnlineCertifyResp() {
	}

	public LayerOnlineCertifyResp(ResponseBody layer_online_certify_response) {
		this.layer_online_certify_response = layer_online_certify_response;
	}

	public ResponseBody getLayer_online_certify_response() {
		return layer_online_certify_response;
	}

	public void setLayer_online_certify_response(
			ResponseBody layer_online_certify_response) {
		this.layer_online_certify_response = layer_online_certify_response;
	}
	
}
