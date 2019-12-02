package com.sumavision.tetris.cs.channel.broad.file;

import com.sumavision.tetris.mvc.converter.AbstractBaseVO;

public class BroadFileBroadInfoVO extends AbstractBaseVO<BroadFileBroadInfoVO, BroadFileBroadInfoPO>{
	
	/** 频道id */
	private Long channelId;
	
	/** 预播发用户id */
	private Long userId;
	
	/** 预播发用户ip */
	private String userIp;
	
	/** 预播发用户类型 */
	private String userEquipType;

	public Long getChannelId() {
		return channelId;
	}

	public BroadFileBroadInfoVO setChannelId(Long channelId) {
		this.channelId = channelId;
		return this;
	}

	public Long getUserId() {
		return userId;
	}

	public BroadFileBroadInfoVO setUserId(Long userId) {
		this.userId = userId;
		return this;
	}

	public String getUserIp() {
		return userIp;
	}

	public BroadFileBroadInfoVO setUserIp(String userIp) {
		this.userIp = userIp;
		return this;
	}

	public String getUserEquipType() {
		return userEquipType;
	}

	public BroadFileBroadInfoVO setUserEquipType(String userEquipType) {
		this.userEquipType = userEquipType;
		return this;
	}

	@Override
	public BroadFileBroadInfoVO set(BroadFileBroadInfoPO entity) throws Exception {
		this.setId(entity.getId())
		.setUuid(entity.getUuid())
		.setUserIp(entity.getUserIp())
		.setUserEquipType(entity.getUserEquipType())
		.setChannelId(entity.getChannelId())
		.setUserId(entity.getUserId());
		return this;
	}
	
	
	@Override
	public boolean equals(Object obj) {
		BroadFileBroadInfoVO objVO = (BroadFileBroadInfoVO)obj;
		return (this.getId() != null && this.getId() == objVO.getId())
				|| (this.getUserId() == objVO.getUserId() && this.getUserIp() == objVO.getUserIp());
	}
}
