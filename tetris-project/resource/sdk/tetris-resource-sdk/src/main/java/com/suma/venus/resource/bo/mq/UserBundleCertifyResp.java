package com.suma.venus.resource.bo.mq;

import com.suma.venus.resource.base.bo.BundleCertifyResponseBody;

public class UserBundleCertifyResp{

	private BundleCertifyResponseBody user_bundle_certify_response;
	
	public UserBundleCertifyResp() {
	}
	
	public UserBundleCertifyResp(BundleCertifyResponseBody user_bundle_certify_response) {
		this.user_bundle_certify_response = user_bundle_certify_response;
	}

	public BundleCertifyResponseBody getUser_bundle_certify_response() {
		return user_bundle_certify_response;
	}

	public void setUser_bundle_certify_response(
			BundleCertifyResponseBody user_bundle_certify_response) {
		this.user_bundle_certify_response = user_bundle_certify_response;
	}
	
}
