package com.sumavision.tetris.cs.channel.broad.file;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.sumavision.tetris.orm.po.AbstractBasePO;

@Entity
@Table(name = "TETRIS_CS_FILE_BROAD_INFO")
public class BroadFileBroadInfoPO extends AbstractBasePO{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/** 频道id */
	private Long channelId;
	
	/** 预播发用户id */
	private Long userId;
	
	/** 预播发用户ip */
	private String userIp;
	
	/** 预播发用户类型 */
	private String userEquipType;

	@Column(name = "CHANNEL_ID")
	public Long getChannelId() {
		return channelId;
	}

	public void setChannelId(Long channelId) {
		this.channelId = channelId;
	}

	@Column(name = "USER_ID")
	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	@Column(name = "USER_IP")
	public String getUserIp() {
		return userIp;
	}

	public void setUserIp(String userIp) {
		this.userIp = userIp;
	}

	@Column(name = "USER_EQUIP_TYPE")
	public String getUserEquipType() {
		return userEquipType;
	}

	public void setUserEquipType(String userEquipType) {
		this.userEquipType = userEquipType;
	}
}
