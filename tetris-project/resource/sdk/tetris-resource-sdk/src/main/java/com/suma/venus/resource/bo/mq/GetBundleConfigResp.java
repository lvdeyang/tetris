package com.suma.venus.resource.bo.mq;

import com.suma.venus.resource.base.bo.BundleResponseBody;

public class GetBundleConfigResp{

	private BundleResponseBody get_bundle_config_response;
	
	public GetBundleConfigResp() {
	}

	public GetBundleConfigResp(BundleResponseBody get_bundle_config_response) {
		this.get_bundle_config_response = get_bundle_config_response;
	}

	public BundleResponseBody getGet_bundle_config_response() {
		return get_bundle_config_response;
	}

	public void setGet_bundle_config_response(
			BundleResponseBody get_bundle_config_response) {
		this.get_bundle_config_response = get_bundle_config_response;
	}
	
}
