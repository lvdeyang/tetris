package com.sumavision.bvc.device.monitor.message;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import com.sumavision.tetris.orm.po.AbstractBasePO;

/**
 * 用户被叫消息<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2019年5月21日 下午3:36:19
 */
@Entity
@Table(name = "BVC_MONITOR_CALLED_MESSAGE")
public class MonitorCalledMessagePO extends AbstractBasePO{

	private static final long serialVersionUID = 1L;
	
	/** 主叫用户 */
	private String callUser;
	
	/** 被叫用户 */
	private String receiveUser;
	
	/** 用户绑定的设备所在的接入层 */
	private String layerId;
	
	/** 消息 */
	private String message;
	
	/** 联网业务用户id */
	private Long networkUserId;

	@Column(name = "CALL_USER")
	public String getCallUser() {
		return callUser;
	}

	public void setCallUser(String callUser) {
		this.callUser = callUser;
	}

	@Column(name = "RECEIVE_USER", unique = true)
	public String getReceiveUser() {
		return receiveUser;
	}

	public void setReceiveUser(String receiveUser) {
		this.receiveUser = receiveUser;
	}

	@Column(name = "LAYER_ID")
	public String getLayerId() {
		return layerId;
	}

	public void setLayerId(String layerId) {
		this.layerId = layerId;
	}

	@Column(name = "MESSAGE")
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Column(name = "NETWORK_USER_ID")
	public Long getNetworkUserId() {
		return networkUserId;
	}

	public void setNetworkUserId(Long networkUserId) {
		this.networkUserId = networkUserId;
	}
	
}
