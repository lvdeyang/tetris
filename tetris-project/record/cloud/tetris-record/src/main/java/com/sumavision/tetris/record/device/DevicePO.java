package com.sumavision.tetris.record.device;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import com.sumavision.tetris.orm.po.AbstractBasePO;

@Entity
@Table(name = "record_device")
public class DevicePO extends AbstractBasePO {

	private static final long serialVersionUID = 1L;

	@Column(name = "deviceName")
	private String deviceName;

	@Column(name = "deviceIP")
	private String deviceIP;

	@Column(name = "devicePort")
	private Integer devicePort;

	@Column(name = "deviceUuid")
	private String deviceUuid;

	@Enumerated(EnumType.STRING)
	@Column(name = "onlineStatus")
	private EDeviceOnlineStatus onlineStatus;

	public enum EDeviceOnlineStatus {
		ONLINE("在线"), OFFLINE("离线");

		private String name;

		private EDeviceOnlineStatus(String name) {
			this.name = name;
		}

		public String getName() {
			return this.name;
		}
		
		public static EDeviceOnlineStatus fromString(String s) throws Exception {
			if ("ONLINE".equals(s)) {
				return ONLINE;
			} else if ("OFFLINE".equals(s)) {
				return OFFLINE;
			} else {
				throw new Exception("错误的状态类型：" + s);
			}
		}
	}

	public Integer getDevicePort() {
		return devicePort;
	}

	public void setDevicePort(Integer devicePort) {
		this.devicePort = devicePort;
	}

	public String getDeviceUuid() {
		return deviceUuid;
	}

	public void setDeviceUuid(String deviceUuid) {
		this.deviceUuid = deviceUuid;
	}

	public String getDeviceIP() {
		return deviceIP;
	}

	public void setDeviceIP(String deviceIP) {
		this.deviceIP = deviceIP;
	}

	public String getDeviceName() {
		return deviceName;
	}

	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}

	public EDeviceOnlineStatus getOnlineStatus() {
		return onlineStatus;
	}

	public void setOnlineStatus(EDeviceOnlineStatus onlineStatus) {
		this.onlineStatus = onlineStatus;
	}

}
