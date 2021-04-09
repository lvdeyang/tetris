package com.sumavision.bvc.control.device.jv230.vo;

import com.sumavision.bvc.device.jv230.po.Jv230ChannelPO;
import com.sumavision.tetris.mvc.converter.AbstractBaseVO;

public class DecodeVo extends AbstractBaseVO<DecodeVo, Jv230ChannelPO>{

	//解码编号
	private Long decodeNum;

	public Long getDecodeNum() {
		return decodeNum;
	}

	public void setDecodeNum(Long decodeNum) {
		this.decodeNum = decodeNum;
	}

	@Override
	public DecodeVo set(Jv230ChannelPO entity) throws Exception {
		this.setDecodeNum(entity.getSerialNum());
		return this;
	}	
}
