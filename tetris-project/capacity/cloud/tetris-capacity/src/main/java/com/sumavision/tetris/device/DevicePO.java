package com.sumavision.tetris.device;

import com.sumavision.tetris.business.common.enumeration.BackType;
import com.sumavision.tetris.business.common.enumeration.FunUnitStatus;
import com.sumavision.tetris.orm.po.AbstractBasePO;

import javax.persistence.*;

@Entity
@Table(name="TETRIS_CAPACITY_DEVICE")
public class DevicePO extends AbstractBasePO {


	/**
	 * 设备名称
	 */
	private String name;

	/**
	 * 设备地址
	 */
	private String deviceIp;

	/**
	 * 设备端口
	 */
	private Integer devicePort;

	/**所属设备分组ID*/
	private Long deviceGroupId;

	private FunUnitStatus funUnitStatus;

	private BackType backType;

	/**
	 * 设备网卡是否已配置
	 */
	private Boolean netConfig = false;

	private String bundleId;

	@Column
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	@Column
	public String getDeviceIp() {
		return deviceIp;
	}

	public void setDeviceIp(String deviceIp) {
		this.deviceIp = deviceIp;
	}

	@Column
	public Long getDeviceGroupId() {
		return deviceGroupId;
	}

	public void setDeviceGroupId(Long deviceGroupId) {
		this.deviceGroupId = deviceGroupId;
	}

	@Column
	@Enumerated(EnumType.STRING)
	public FunUnitStatus getFunUnitStatus() {
		return funUnitStatus;
	}

	public void setFunUnitStatus(FunUnitStatus funUnitStatus) {
		this.funUnitStatus = funUnitStatus;
	}

	@Column
	public Integer getDevicePort() {
		return devicePort;
	}

	public void setDevicePort(Integer devicePort) {
		this.devicePort = devicePort;
	}

	@Column
	@Enumerated(EnumType.STRING)
	public BackType getBackType() {
		return backType;
	}

	public void setBackType(BackType backType) {
		this.backType = backType;
	}

	public Boolean getNetConfig() {
		return netConfig;
	}

	public void setNetConfig(Boolean netConfig) {
		this.netConfig = netConfig;
	}

	public String getBundleId() {
		return bundleId;
	}

	@Column
	public void setBundleId(String bundleId) {
		this.bundleId = bundleId;
	}
}
