package com.sumavision.signal.bvc.capacity.bo.output;

import java.io.Serializable;

public class CommonTsOutputBO extends BaseTsOutputBO<CommonTsOutputBO> implements Serializable {

    private static final long serialVersionUID = 1131430716211930430L;

	public CommonTsOutputBO setUdp_ts(){
		this.setRate_ctrl("VBR")
			.setBitrate(8000000);
		return this;
	}
	
}
