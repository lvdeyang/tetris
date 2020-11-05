package com.sumavision.bvc.control.system.vo;

import com.sumavision.bvc.system.po.ChannelNamePO;
import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.mvc.converter.AbstractBaseVO;

/**
 * @ClassName: 通道别名
 * @author zy
 * @date 2018年7月24日 下午8:20:56
 */
public class ChannelNameVO extends AbstractBaseVO<ChannelNameVO, ChannelNamePO>{

	/** 别名 */
	private String name;
	
	/** 通道名称 */
	private String channelName;
	
	/** 通道类型 */
	private String channelType;
	
	public String getName() {
		return name;
	}

	public ChannelNameVO setName(String name) {
		this.name = name;
		return this;
	}

	public String getChannelName() {
		return channelName;
	}

	public ChannelNameVO setChannelName(String channelName) {
		this.channelName = channelName;
		return this;
	}

	public String getChannelType() {
		return channelType;
	}
	
	public ChannelNameVO setChannelType(String channelType) {
		this.channelType = channelType;
		return this;
	}

	@Override
	public ChannelNameVO set(ChannelNamePO entity) throws Exception {
		this.setId(entity.getId())
			.setUuid(entity.getUuid())
			.setUpdateTime(entity.getUpdateTime()==null?"":DateUtil.format(entity.getUpdateTime(), DateUtil.dateTimePattern))
			.setName(entity.getName())
			.setChannelName(entity.getChannelName())
			.setChannelType(entity.getChannelType());
		return this;
	}
	
}
