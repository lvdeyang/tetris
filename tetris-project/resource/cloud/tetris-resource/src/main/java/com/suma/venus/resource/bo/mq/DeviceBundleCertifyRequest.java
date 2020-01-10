package com.suma.venus.resource.bo.mq;

import com.suma.venus.resource.base.bo.DeviceBundleCertifyParam;

/**
 * 设备特征bundle认证请求消息体
 * @author lxw
 *
 */
public class DeviceBundleCertifyRequest{

	private DeviceBundleCertifyParam device_bundle_certify_request;

	public DeviceBundleCertifyParam getDevice_bundle_certify_request() {
		return device_bundle_certify_request;
	}

	public void setDevice_bundle_certify_request(
			DeviceBundleCertifyParam device_bundle_certify_request) {
		this.device_bundle_certify_request = device_bundle_certify_request;
	}
	
}
