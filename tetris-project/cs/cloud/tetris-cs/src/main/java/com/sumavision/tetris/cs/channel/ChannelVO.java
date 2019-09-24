package com.sumavision.tetris.cs.channel;

import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.mvc.converter.AbstractBaseVO;

public class ChannelVO extends AbstractBaseVO<ChannelVO, ChannelPO> {

	private String name;

	private String date;

	private String remark;
	
	private String broadWay;
	
	private String broadcastStatus;
	
	private String groupId;
	
	private String previewUrlIp;
	
	private String previewUrlPort;
	
	private Integer broadId;
	
	private String type;

	@Override
	public ChannelVO set(ChannelPO entity) throws Exception {
		this.setId(entity.getId())
		.setUuid(entity.getUuid())
		.setUpdateTime(entity.getUpdateTime() == null ? "": DateUtil.format(entity.getUpdateTime(), DateUtil.dateTimePattern))
		.setName(entity.getName())
		.setDate(entity.getDate())
		.setRemark(entity.getRemark())
		.setBroadWay(entity.getBroadWay())
		.setBroadcastStatus(entity.getBroadcastStatus())
		.setGroupId(entity.getGroupId())
		.setPreviewUrlIp(entity.getPreviewUrlIp())
		.setPreviewUrlPort(entity.getPreviewUrlPort())
		.setType(entity.getType())
		.setBroadId(entity.getBroadId());

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

	public String getBroadWay() {
		return broadWay;
	}

	public ChannelVO setBroadWay(String broadWay) {
		this.broadWay = broadWay;
		return this;
	}

	public String getBroadcastStatus() {
		return broadcastStatus;
	}

	public ChannelVO setBroadcastStatus(String broadcastStatus) {
		this.broadcastStatus = broadcastStatus;
		return this;
	}

	public String getGroupId() {
		return groupId;
	}

	public ChannelVO setGroupId(String groupId) {
		this.groupId = groupId;
		return this;
	}

	public String getPreviewUrlIp() {
		return previewUrlIp;
	}

	public ChannelVO setPreviewUrlIp(String previewUrlIp) {
		this.previewUrlIp = previewUrlIp;
		return this;
	}

	public String getPreviewUrlPort() {
		return previewUrlPort;
	}

	public ChannelVO setPreviewUrlPort(String previewUrlPort) {
		this.previewUrlPort = previewUrlPort;
		return this;
	}

	public Integer getBroadId() {
		return broadId;
	}

	public ChannelVO setBroadId(Integer broadId) {
		this.broadId = broadId;
		return this;
	}

	public String getType() {
		return type;
	}

	public ChannelVO setType(String type) {
		this.type = type;
		return this;
	}
}
