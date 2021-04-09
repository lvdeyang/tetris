package com.sumavision.bvc.device.monitor.live;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.sumavision.tetris.orm.po.AbstractBasePO;

/**
 * 用户视频通话任务<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2019年5月21日 下午4:13:33
 */
@Entity
@Table(name = "BVC_MONITOR_USER_CALL")
public class MonitorUserCallPO extends AbstractBasePO{

	private static final long serialVersionUID = 1L;
	
	/** 主叫用户 */
	private String srcUser;
	
	/** 被叫用户 */
	private String dstUser;
	
	/** 正向直播任务 */
	private Long liveId;
	
	/** 反向直播任务 */
	private Long reverseLiveId;
	
	/** 标识通话类型：play点播，call呼叫 */
	private String type;
	
	@Column(name = "SRC_USER")
	public String getSrcUser() {
		return srcUser;
	}

	public void setSrcUser(String srcUser) {
		this.srcUser = srcUser;
	}

	@Column(name = "DST_USER")
	public String getDstUser() {
		return dstUser;
	}

	public void setDstUser(String dstUser) {
		this.dstUser = dstUser;
	}

	@Column(name = "LIVE_ID")
	public Long getLiveId() {
		return liveId;
	}

	public void setLiveId(Long liveId) {
		this.liveId = liveId;
	}

	@Column(name = "REVERSE_LIVE_ID")
	public Long getReverseLiveId() {
		return reverseLiveId;
	}

	public void setReverseLiveId(Long reverseLiveId) {
		this.reverseLiveId = reverseLiveId;
	}

	@Column(name = "TYPE")
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
}
