package com.sumavision.tetris.cs.channel;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;


import com.sumavision.tetris.orm.po.AbstractBasePO;

@Table
@Entity(name = "TETRIS_CS_CHANNEL")
public class ChannelPO extends AbstractBasePO {
	private static final long serialVersionUID = 1L;
	
	/** 唯一标识 */
	public static final String VERSION_OF_ORIGIN = "0.0";

	/** 频道名称 */
	private String name;
	
	/** 频道生效时间 */
	private String date;
	
	/** 频道备注 */
	private String remark;
	
	/** 播发方式 */
	private String broadWay;
	
	/** 播发id */
	private Integer abilityBroadId;
	
	/** 播发状态 */
	private String broadcastStatus;
	
	/** 用户组织信息 */
	private String groupId;
	
	/** 频道类型 */
	private String type;
	
	/** 是否加密 */
	private Boolean encryption;
	
	/** 是否自动播发 */
	private Boolean autoBroad;
	
	/** 频道uuid标识--流程里面使用 */
	private String channelUuid;
	
	/**转码模板*/
	private String taskTemple;
	
	/**码率控制*/
	private String rateCtrl;
	
	/**码率*/
	private String rate;
	
	/**是否轮播*/
	private boolean rotation;
	
	public boolean isRotation() {
		return rotation;
	}

	public void setRotation(boolean rotation) {
		this.rotation = rotation;
	}

	public enum RATE_CTRL {
		VBR, CBR
	}

	@Column(name = "TASK_TEMPLE")
	public String getTaskTemple() {
		return taskTemple;
	}

	public void setTaskTemple(String taskTemple) {
		this.taskTemple = taskTemple;
	}

	@Column(name = "RATE_CTRL")
	public String getRateCtrl() {
		return rateCtrl;
	}

	public void setRateCtrl(String rateCtrl) {
		this.rateCtrl = rateCtrl;
	}

	@Column(name = "RATE")
	public String getRate() {
		return rate;
	}

	public void setRate(String rate) {
		this.rate = rate;
	}

	@Column(name = "NAME")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "DATE")
	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	@Column(name = "BROAD_WAY")
	public String getBroadWay() {
		return broadWay;
	}

	public void setBroadWay(String broadWay) {
		this.broadWay = broadWay;
	}

	@Column(name = "ABILITY_BROAD_ID")
	public Integer getAbilityBroadId() {
		return abilityBroadId;
	}

	public void setAbilityBroadId(Integer abilityBroadId) {
		this.abilityBroadId = abilityBroadId;
	}

	@Column(name = "REMARK")
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Column(name = "BROADCAST_STATUS")
	public String getBroadcastStatus() {
		return broadcastStatus;
	}

	public void setBroadcastStatus(String broadcastStatus) {
		this.broadcastStatus = broadcastStatus;
	}

	@Column(name = "GROUP_ID")
	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	@Column(name = "TYPE")
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Column(name = "ENCTYPTION")
	public Boolean getEncryption() {
		return encryption;
	}

	public void setEncryption(Boolean encryption) {
		this.encryption = encryption;
	}

	@Column(name = "AUTO_BROAD")
	public Boolean getAutoBroad() {
		return autoBroad;
	}

	public void setAutoBroad(Boolean autoBroad) {
		this.autoBroad = autoBroad;
	}

	@Column(name = "CHANNEL_UUID")
	public String getChannelUuid() {
		return channelUuid;
	}

	public void setChannelUuid(String channelUuid) {
		this.channelUuid = channelUuid;
	}
}
