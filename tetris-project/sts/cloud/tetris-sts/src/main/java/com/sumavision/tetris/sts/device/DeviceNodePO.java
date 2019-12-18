package com.sumavision.tetris.sts.device;



import javax.persistence.*;

import com.sumavision.tetris.sts.common.CommonConstants.BackType;
import com.sumavision.tetris.sts.common.CommonConstants.DeviceType;
import com.sumavision.tetris.sts.common.CommonConstants.FunUnitStatus;
import com.sumavision.tetris.sts.common.CommonConstants.FunUnitType;
import com.sumavision.tetris.sts.common.CommonConstants.LockStatus;
import com.sumavision.tetris.sts.common.CommonPO;
import com.sumavision.tetris.sts.common.SystemCtrl;
import com.sumavision.tetris.sts.task.nCard.EncapsulateAuthPO;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 硬件设备内的节点，Device与DeviceNode是一对多的关系
 * @author gaofeng
 *
 */
@Entity
@Table
public class DeviceNodePO extends CommonPO<DeviceNodePO> implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 562521546626264029L;
	
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

	private DeviceType deviceType;

	private EncapsulateAuthPO encapsulateAuthPO;

	private BackType encapsulateBackType;

	private FunUnitStatus encapsulateStatus;
	
	private LockStatus encapsulateLockStatus;

	private String encapsulateVersion;

	private String encapsulateSocketAddress;

	private Integer backIndex;

	private BackType transBackType;

	private FunUnitStatus transStatus;
	
	private LockStatus transLockStatus;

	private String transVersion;

	private String transSocketAddress;
	
	private Integer nCardNum;
	
	private List<DeviceChannelAuthPO> deviceChannelAuthPOs = new ArrayList<DeviceChannelAuthPO>();
	
	public DeviceNodePO() {
		this.encapsulateBackType = BackType.DEFAULT;
		this.transBackType = BackType.DEFAULT;
		this.encapsulateStatus = FunUnitStatus.NONE;
		this.transStatus = FunUnitStatus.NONE;
		this.encapsulateLockStatus = LockStatus.UNLOCKED;
		this.transLockStatus = LockStatus.UNLOCKED;
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
	@Enumerated(EnumType.STRING)
	public DeviceType getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(DeviceType deviceType) {
		this.deviceType = deviceType;
	}

	@Column
	@Enumerated(EnumType.STRING)
	public BackType getEncapsulateBackType() {
		return encapsulateBackType;
	}

	public void setEncapsulateBackType(BackType encapsulateBackType) {
		this.encapsulateBackType = encapsulateBackType;
	}

	@Column
	public Integer getBackIndex() {
		return backIndex;
	}

	public void setBackIndex(Integer backIndex) {
		this.backIndex = backIndex;
	}

	@Column
	@Enumerated(EnumType.STRING)
	public BackType getTransBackType() {
		return transBackType;
	}

	public void setTransBackType(BackType transBackType) {
		this.transBackType = transBackType;
	}

	@Column
	@Enumerated(EnumType.STRING)
	public FunUnitStatus getEncapsulateStatus() {
		return encapsulateStatus;
	}

	@Deprecated
	public void setEncapsulateStatus(FunUnitStatus encapsulateStatus) {
		this.encapsulateStatus = encapsulateStatus;
	}

	@Column
	@Enumerated(EnumType.STRING)
	public FunUnitStatus getTransStatus() {
		return transStatus;
	}

	@Deprecated
	public void setTransStatus(FunUnitStatus transStatus) {
		this.transStatus = transStatus;
	}

	@Column
	public String getEncapsulateVersion() {
		return encapsulateVersion;
	}

	public void setEncapsulateVersion(String encapsulateVersion) {
		this.encapsulateVersion = encapsulateVersion;
	}

	@Column
	public String getTransVersion() {
		return transVersion;
	}

	public void setTransVersion(String transVersion) {
		this.transVersion = transVersion;
	}

	public void setVersion(String version , FunUnitType funUnitType) {
		if (funUnitType.equals(FunUnitType.TRANS))
			this.transVersion = version;
		else if (funUnitType.equals(FunUnitType.ENCAPSULATE))
			this.encapsulateVersion = version;
	}

	@Transient
	public String getSocketAddress(FunUnitType funUnitType) {
		if (funUnitType.equals(FunUnitType.TRANS))
			return SystemCtrl.getTransAddress(this.deviceIp);
		else if (funUnitType.equals(FunUnitType.ENCAPSULATE))
			return SystemCtrl.getPacketAddress(this.deviceIp);
		return null;
	}

	@Column
	public String getEncapsulateSocketAddress() {
		return encapsulateSocketAddress;
	}

	public void setEncapsulateSocketAddress(String encapsulateSocketAddress) {
		this.encapsulateSocketAddress = encapsulateSocketAddress;
	}

	@Column
	public String getTransSocketAddress() {
		return transSocketAddress;
	}

	public void setTransSocketAddress(String transSocketAddress) {
		this.transSocketAddress = transSocketAddress;
	}

	@Column
	public Integer getnCardNum() {
		return nCardNum;
	}

	public void setnCardNum(Integer nCardNum) {
		this.nCardNum = nCardNum;
	}
	
	@Column
	public Long getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(Long deviceId) {
		this.deviceId = deviceId;
	}

	@Column
	@Enumerated(EnumType.STRING)
	public LockStatus getEncapsulateLockStatus() {
		return encapsulateLockStatus;
	}

	public void setEncapsulateLockStatus(LockStatus encapsulateLockStatus) {
		this.encapsulateLockStatus = encapsulateLockStatus;
	}

	@Column
	@Enumerated(EnumType.STRING)
	public LockStatus getTransLockStatus() {
		return transLockStatus;
	}

	public void setTransLockStatus(LockStatus transLockStatus) {
		this.transLockStatus = transLockStatus;
	}

	public void addUnit(FunUnitType type) {
		switch (type) {
			case TRANS:
				this.transBackType = BackType.MAIN;
				this.transStatus = FunUnitStatus.NORMAL;
				this.transSocketAddress = SystemCtrl.getSocketAddress(this.deviceIp , FunUnitType.TRANS);
				break;
			case ENCAPSULATE:
				if (this.deviceType.equals(DeviceType.SERVER))
					this.encapsulateBackType = BackType.MAIN;
				this.encapsulateStatus = FunUnitStatus.NORMAL;
				this.encapsulateSocketAddress = SystemCtrl.getSocketAddress(this.deviceIp , FunUnitType.ENCAPSULATE);
				break;
			default:
				break;
		}
	}

	public void deleteUnit(FunUnitType type) {
		switch (type) {
			case TRANS:
//				this.transBackType = BackType.DEFAULT;
				this.transStatus = FunUnitStatus.NONE;
				this.transSocketAddress = null;
				this.transVersion = null;
				break;
			case ENCAPSULATE:
				this.encapsulateBackType = BackType.DEFAULT;
				this.backIndex = null;
				this.encapsulateStatus = FunUnitStatus.NONE;
				this.encapsulateSocketAddress = null;
				this.encapsulateVersion = null;
				break;
			default:
				break;
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
		result = prime * result + ((backIndex == null) ? 0 : backIndex.hashCode());
		result = prime * result + ((deviceGroupId == null) ? 0 : deviceGroupId.hashCode());
		result = prime * result + ((deviceId == null) ? 0 : deviceId.hashCode());
		result = prime * result + ((deviceIp == null) ? 0 : deviceIp.hashCode());
		result = prime * result + ((deviceType == null) ? 0 : deviceType.hashCode());
		result = prime * result + ((encapsulateBackType == null) ? 0 : encapsulateBackType.hashCode());
		result = prime * result + ((encapsulateLockStatus == null) ? 0 : encapsulateLockStatus.hashCode());
		result = prime * result + ((encapsulateSocketAddress == null) ? 0 : encapsulateSocketAddress.hashCode());
		result = prime * result + ((encapsulateStatus == null) ? 0 : encapsulateStatus.hashCode());
		result = prime * result + ((encapsulateVersion == null) ? 0 : encapsulateVersion.hashCode());
		result = prime * result + ((nCardNum == null) ? 0 : nCardNum.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((position == null) ? 0 : position.hashCode());
		result = prime * result + ((transBackType == null) ? 0 : transBackType.hashCode());
		result = prime * result + ((transLockStatus == null) ? 0 : transLockStatus.hashCode());
		result = prime * result + ((transSocketAddress == null) ? 0 : transSocketAddress.hashCode());
		result = prime * result + ((transStatus == null) ? 0 : transStatus.hashCode());
		result = prime * result + ((transVersion == null) ? 0 : transVersion.hashCode());
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
		if (!getId().equals(other.getId())) {
			return false;
		}
		if (backIndex == null) {
			if (other.backIndex != null)
				return false;
		} else if (!backIndex.equals(other.backIndex))
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
		if (deviceType != other.deviceType)
			return false;
		if (encapsulateBackType != other.encapsulateBackType)
			return false;
		if (encapsulateLockStatus != other.encapsulateLockStatus)
			return false;
		if (encapsulateSocketAddress == null) {
			if (other.encapsulateSocketAddress != null)
				return false;
		} else if (!encapsulateSocketAddress.equals(other.encapsulateSocketAddress))
			return false;
		if (encapsulateStatus != other.encapsulateStatus)
			return false;
		if (encapsulateVersion == null) {
			if (other.encapsulateVersion != null)
				return false;
		} else if (!encapsulateVersion.equals(other.encapsulateVersion))
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
		if (position == null) {
			if (other.position != null)
				return false;
		} else if (!position.equals(other.position))
			return false;
		if (transBackType != other.transBackType)
			return false;
		if (transLockStatus != other.transLockStatus)
			return false;
		if (transSocketAddress == null) {
			if (other.transSocketAddress != null)
				return false;
		} else if (!transSocketAddress.equals(other.transSocketAddress))
			return false;
		if (transStatus != other.transStatus)
			return false;
		if (transVersion == null) {
			if (other.transVersion != null)
				return false;
		} else if (!transVersion.equals(other.transVersion))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "DeviceNodePO{" +
				"name='" + name + '\'' +
				", deviceIp='" + deviceIp + '\'' +
				", deviceGroupId=" + deviceGroupId +
				", deviceId=" + deviceId +
				", position=" + position +
				", deviceType=" + deviceType +
				", encapsulateBackType=" + encapsulateBackType +
				", encapsulateStatus=" + encapsulateStatus +
				", encapsulateLockStatus=" + encapsulateLockStatus +
				", encapsulateVersion='" + encapsulateVersion + '\'' +
				", encapsulateSocketAddress='" + encapsulateSocketAddress + '\'' +
				", backIndex=" + backIndex +
				", transBackType=" + transBackType +
				", transStatus=" + transStatus +
				", transLockStatus=" + transLockStatus +
				", transVersion='" + transVersion + '\'' +
				", transSocketAddress='" + transSocketAddress + '\'' +
				", nCardNum=" + nCardNum +
				'}';
	}
}
