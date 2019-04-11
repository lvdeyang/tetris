package com.sumavision.tetris.cs.channel;

import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.mvc.converter.AbstractBaseVO;

public class ChannelVO extends AbstractBaseVO<ChannelVO, ChannelPO> {

	private String name;

	private String date;

	private String remark;
	
	private String broadcastStatus;

	@Override
	public ChannelVO set(ChannelPO entity) throws Exception {
		this.setId(entity.getId())
		.setUuid(entity.getUuid())
		.setUpdateTime(entity.getUpdateTime() == null ? "": DateUtil.format(entity.getUpdateTime(), DateUtil.dateTimePattern))
		.setName(entity.getName())
		.setDate(entity.getDate())
		.setRemark(entity.getRemark())
		.setBroadcastStatus(entity.getBroadcastStatus());

		return this;
	}

	public String getName() {
		return name;
	}

	public ChannelVO setName(String name) {
		this.name = name;
		return this;
	}

	public String getRemark() {
		return remark;
	}

	public ChannelVO setRemark(String remark) {
		this.remark = remark;
		return this;
	}

	public String getDate() {
		return date;
	}

	public ChannelVO setDate(String date) {
		this.date = date;
		return this;
	}

	public String getBroadcastStatus() {
		return broadcastStatus;
	}

	public ChannelVO setBroadcastStatus(String broadcastStatus) {
		this.broadcastStatus = broadcastStatus;
		return this;
	}
}
