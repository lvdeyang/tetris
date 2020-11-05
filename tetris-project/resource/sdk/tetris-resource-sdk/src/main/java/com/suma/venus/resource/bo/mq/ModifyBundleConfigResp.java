package com.suma.venus.resource.bo.mq;

import com.suma.venus.message.mq.ResponseBO;
import com.suma.venus.resource.base.bo.ResponseBody;
import com.suma.venus.resource.constant.MQConstant;

public class ModifyBundleConfigResp extends ResponseBO{

	public ModifyBundleConfigResp() {
		super();
		this.getMessage().getMessage_header().setMessage_name(MQConstant.MODIFY_BUNDLE_CONFIG_RESPONSE);
	}

	public ModifyBundleConfigResp(int result, String dstID, String requestID,ResponseBody modify_bundle_config_response) {
		super(result, dstID, requestID);
		this.getMessage().getMessage_header().setMessage_name(MQConstant.MODIFY_BUNDLE_CONFIG_RESPONSE);
		this.getMessage().getMessage_body().put("modify_bundle_config_response", modify_bundle_config_response);
	}

}
