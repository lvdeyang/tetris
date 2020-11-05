package com.sumavision.tetris.cs.template;

import com.sumavision.tetris.cs.channel.ChannelPO;
import com.sumavision.tetris.cs.channel.ChannelVO;
import com.sumavision.tetris.mvc.converter.AbstractBaseVO;

public class ChannelTemplateVo extends AbstractBaseVO<ChannelTemplateVo, ChannelTemplatePO> {

	private String name;
	
	
	
	public String getName() {
		return name;
	}



	public void setName(String name) {
		this.name = name;
	}



	@Override
	public ChannelTemplateVo set(ChannelTemplatePO entity) throws Exception {
		// TODO Auto-generated method stub
		this.setId(entity.getId());
		this.setName(entity.getName());
		return this;
	}

}
