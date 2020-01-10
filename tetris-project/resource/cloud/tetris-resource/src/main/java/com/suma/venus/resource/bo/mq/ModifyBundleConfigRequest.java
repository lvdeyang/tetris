package com.suma.venus.resource.bo.mq;

import com.suma.venus.message.bo.RequestBaseBO;
import com.suma.venus.resource.constant.MQConstant;

public class ModifyBundleConfigRequest extends RequestBaseBO{

	public ModifyBundleConfigRequest() {
		super();
		this.getMessage().getMessage_header().setMessage_name(MQConstant.MODIFY_BUNDLE_CONFIG_REQUEST);
	}

}
