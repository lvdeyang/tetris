package com.sumavision.bvc.device.jv230.bo;

import com.sumavision.bvc.device.jv230.po.Jv230ChannelPO;

public class Jv230ChannelParamBO {

	/** 类型 VideoMatrixVideoOut/VideoMatrixAudioOut*/
	private String base_type;
	
	private int base_version = 10000;
	
	private Jv230BaseParamBO base_param;

	public String getBase_type() {
		return base_type;
	}

	public Jv230ChannelParamBO setBase_type(String base_type) {
		this.base_type = base_type;
		return this;
	}

	public int getBase_version() {
		return base_version;
	}

	public Jv230ChannelParamBO setBase_version(int base_version) {
		this.base_version = base_version;
		return this;
	}

	public Jv230BaseParamBO getBase_param() {
		return base_param;
	}

	public Jv230ChannelParamBO setBase_param(Jv230BaseParamBO base_param) {
		this.base_param = base_param;
		return this;
	}
	
	public Jv230ChannelParamBO set(Jv230ChannelPO channel, Jv230BaseParamBO base_param){
		this.setBase_type(channel.getChannelType())
			.setBase_param(base_param);
		
		return this;
	}		
}
