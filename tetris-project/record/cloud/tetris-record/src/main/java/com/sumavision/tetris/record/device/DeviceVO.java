package com.sumavision.tetris.record.device;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;

import com.sumavision.tetris.record.device.DevicePO.EDeviceOnlineStatus;

public class DeviceVO {
	private Long id;

	private String deviceName;

	private String deviceIP;

	private Integer devicePort;

	private String onlineStatus;

	public static DeviceVO transFromPO(DevicePO po) {
		if (po == null) {
			return null;
		}

		DeviceVO vo = new DeviceVO();
		BeanUtils.copyProperties(po, vo);
		vo.setOnlineStatus(po.getOnlineStatus().getName());

		return vo;

	}

	public static List<DeviceVO> transFromPOs(List<DevicePO> pos) {
		if (pos == null) {
			return null;
		}

		List<DeviceVO> vos = new ArrayList<DeviceVO>();

		for (DevicePO po : pos) {

			vos.add(transFromPO(po));
		}

		return vos;

	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDeviceName() {
		return deviceName;
	}

	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}

	public String getDeviceIP() {
		return deviceIP;
	}

	public void setDeviceIP(String deviceIP) {
		this.deviceIP = deviceIP;
	}

	public Integer getDevicePort() {
		return devicePort;
	}

	public void setDevicePort(Integer devicePort) {
		this.devicePort = devicePort;
	}

	public String getOnlineStatus() {
		return onlineStatus;
	}

	public void setOnlineStatus(String onlineStatus) {
		this.onlineStatus = onlineStatus;
	}

}
