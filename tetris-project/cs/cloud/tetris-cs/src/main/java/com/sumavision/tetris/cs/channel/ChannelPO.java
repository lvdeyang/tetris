package com.sumavision.tetris.cs.channel;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import com.sumavision.tetris.orm.po.AbstractBasePO;

@Table
@Entity(name = "TETRIS_CS_CHANNEL")
public class ChannelPO extends AbstractBasePO {
	private static final long serialVersionUID = 1L;

	/** 频道名称 */
	private String name;
	/** 频道生效时间 */
	private String date;
	/** 频道备注 */
	private String remark;
	/** 播发状态 */
	private String broadcastStatus;

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
}
