package com.sumavision.tetris.cs.channel.autoBroad;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.sumavision.tetris.orm.po.AbstractBasePO;

@Entity
@Table(name = "TETRIS_CS_AUTO_BROAD_INFO")
public class ChannelAutoBroadInfoPO extends AbstractBasePO{

	/**
	 * 轮播推流--智能播发参数
	 */
	private static final long serialVersionUID = 1L;
	
	/** 频道id */
	private Long channelId;
	
	/** 是否乱序 */
	private Boolean shuffle;
	
	/** 生效时长 */
	private Integer duration;
	
	/** 起始时间 */
	private String startTime;
	
	/** 起始日期 */
	private String startDate;

	@Column(name = "CHANNEL_ID")
	public Long getChannelId() {
		return channelId;
	}

	public void setChannelId(Long channelId) {
		this.channelId = channelId;
	}

	@Column(name = "SHUFFLE")
	public Boolean getShuffle() {
		return shuffle;
	}

	public void setShuffle(Boolean shuffle) {
		this.shuffle = shuffle;
	}

	@Column(name = "DURATION")
	public Integer getDuration() {
		return duration;
	}

	public void setDuration(Integer duration) {
		this.duration = duration;
	}

	@Column(name = "START_TIME")
	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	@Column(name = "START_DATE")
	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
}
