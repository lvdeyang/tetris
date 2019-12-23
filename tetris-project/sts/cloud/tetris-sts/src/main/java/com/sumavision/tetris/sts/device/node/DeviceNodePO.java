package com.sumavision.tetris.sts.device.node;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.sumavision.tetris.sts.common.CommonConstants.FunUnitStatus;
import com.sumavision.tetris.sts.common.CommonPO;
import com.sumavision.tetris.sts.device.auth.DeviceChannelAuthPO;
import com.sumavision.tetris.sts.device.auth.EncapsulateAuthPO;

@Entity
@Table
public class DeviceNodePO extends CommonPO<DeviceNodePO> implements Serializable{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3136879526745400074L;

	/**
	 * 设备名称
	 */
	private String name;

	/**
	 * 设备地址
	 */
	private String deviceIp;

	/**所属设备分组ID*/
	private Long deviceGroupId;
	
	/**所属设备ID*/
	private Long deviceId;

	private Integer position;

	/**
	 * 主1，主2
	 */
	private Integer backIndex;

	private EncapsulateAuthPO encapsulateAuthPO;

	private FunUnitStatus funUnitStatus;
	
	private Integer nCardNum;
	
	private String version;
	
	private List<DeviceChannelAuthPO> deviceChannelAuthPOs = new ArrayList<DeviceChannelAuthPO>();
	
	public DeviceNodePO() {
		this.funUnitStatus = FunUnitStatus.NONE;
	}

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

	
	@OneToOne(fetch= FetchType.EAGER,cascade=CascadeType.ALL)
	@JoinColumn(name="encapsulateAuthId")
	public EncapsulateAuthPO getEncapsulateAuthPO() {
		return encapsulateAuthPO == null ? encapsulateAuthPO = new EncapsulateAuthPO() : encapsulateAuthPO;
	}

	public void setEncapsulateAuthPO(EncapsulateAuthPO encapsulateAuthPO) {
		this.encapsulateAuthPO = encapsulateAuthPO;
	}
	
	@OneToMany(fetch= FetchType.EAGER, cascade=CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name="deviceId")
	public List<DeviceChannelAuthPO> getDeviceChannelAuthPOs() {
		return deviceChannelAuthPOs;
	}

	public void setDeviceChannelAuthPOs(List<DeviceChannelAuthPO> deviceChannelAuthPOs) {
		this.deviceChannelAuthPOs = deviceChannelAuthPOs;
	}

	@Column
	public Integer getPosition() {
		return position;
	}

	public void setPosition(Integer position) {
		this.position = position;
	}

	@Column
	public Integer getBackIndex() {
		return backIndex;
	}

	public void setBackIndex(Integer backIndex) {
		this.backIndex = backIndex;
	}

	@Column
	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}


	@Column
	public Integer getnCardNum() {
		return nCardNum;
	}

	public void setnCardNum(Integer nCardNum) {
		this.nCardNum = nCardNum;
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
	public Long getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(Long deviceId) {
		this.deviceId = deviceId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((deviceChannelAuthPOs == null) ? 0 : deviceChannelAuthPOs
						.hashCode());
		result = prime * result
				+ ((deviceGroupId == null) ? 0 : deviceGroupId.hashCode());
		result = prime * result
				+ ((deviceId == null) ? 0 : deviceId.hashCode());
		result = prime * result
				+ ((deviceIp == null) ? 0 : deviceIp.hashCode());
		result = prime
				* result
				+ ((encapsulateAuthPO == null) ? 0 : encapsulateAuthPO
						.hashCode());
		result = prime * result
				+ ((nCardNum == null) ? 0 : nCardNum.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result
				+ ((position == null) ? 0 : position.hashCode());
		result = prime * result + ((funUnitStatus == null) ? 0 : funUnitStatus.hashCode());

		result = prime * result + ((version == null) ? 0 : version.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DeviceNodePO other = (DeviceNodePO) obj;
		if (deviceChannelAuthPOs == null) {
			if (other.deviceChannelAuthPOs != null)
				return false;
		} else if (!deviceChannelAuthPOs.equals(other.deviceChannelAuthPOs))
			return false;
		if (deviceGroupId == null) {
			if (other.deviceGroupId != null)
				return false;
		} else if (!deviceGroupId.equals(other.deviceGroupId))
			return false;
		if (deviceId == null) {
			if (other.deviceId != null)
				return false;
		} else if (!deviceId.equals(other.deviceId))
			return false;
		if (deviceIp == null) {
			if (other.deviceIp != null)
				return false;
		} else if (!deviceIp.equals(other.deviceIp))
			return false;
		if (encapsulateAuthPO == null) {
			if (other.encapsulateAuthPO != null)
				return false;
		} else if (!encapsulateAuthPO.equals(other.encapsulateAuthPO))
			return false;
		if (nCardNum == null) {
			if (other.nCardNum != null)
				return false;
		} else if (!nCardNum.equals(other.nCardNum))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (backIndex == null) {
			if (other.backIndex != null)
				return false;
		} else if (!backIndex.equals(other.backIndex))
			return false;
		if (position == null) {
			if (other.position != null)
				return false;
		} else if (!position.equals(other.position))
			return false;
		if (funUnitStatus != other.funUnitStatus)
			return false;
		if (version == null) {
			if (other.version != null)
				return false;
		} else if (!version.equals(other.version))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "DeviceNodePO [name=" + name + ", deviceIp=" + deviceIp
				+ ", deviceGroupId=" + deviceGroupId + ", deviceId=" + deviceId
				+ ", position=" + position + ", encapsulateAuthPO="
				+ encapsulateAuthPO  + ", funUnitStatus=" + funUnitStatus+", backIndex=" + backIndex
				+ ", nCardNum=" + nCardNum + ", version=" + version
				+ ", deviceChannelAuthPOs=" + deviceChannelAuthPOs + "]";
	}

	

}
