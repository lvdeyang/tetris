package com.sumavision.tetris.cs.channel.broad.terminal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.sumavision.tetris.orm.po.AbstractBasePO;

@Entity
@Table(name = "TETRIS_CS_TERMINAL_BROAD_INFO")
public class BroadTerminalBroadInfoPO extends AbstractBasePO{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** 频道id */
	private Long channelId;
	
	/** 播发优先级 */
	private String level;
	
	/** 播发是否携带文件 */
	private Boolean hasFile;
	
	/** 排期单整体停止时间 */
	private String endDate;

	@Column(name = "CHANNEL_ID")
	public Long getChannelId() {
		return channelId;
	}

	public void setChannelId(Long channelId) {
		this.channelId = channelId;
	}

	@Column(name = "LEVEL")
	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	@Column(name = "HAS_FILE")
	public Boolean getHasFile() {
		return hasFile;
	}

	public void setHasFile(Boolean hasFile) {
		this.hasFile = hasFile;
	}

	@Column(name = "END_DATE")
	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

}
