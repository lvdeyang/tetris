package com.suma.venus.resource.pojo;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import com.suma.venus.resource.pojo.ChannelSchemePO.LockStatus;

/**
 * 能力资源表
 * 
 * @author lxw
 *
 */
@Entity
public class BundlePO extends CommonPO<BundlePO> {

	private String bundleId;

	private String bundleName;

	/** bundle别名 **/
	private String bundleAlias;

	/** 设备账号 */
	private String username;

	/** 认证密码 */
	private String onlinePassword;

	/** 如jv210等 */
	private String deviceModel;

	/** bundle_type,如VenusTerminal等 */
	private String bundleType;

	private String deviceVersion;

	private String deviceIp;

	private Integer devicePort;

	/** 资源所属的接入层节点UID */
	private String accessNodeUid;

	/** 所属分组ID */
	private Long folderId;

	/** 在分组中的排序序号 */
	private Integer folderIndex;

	/** bundle号码 **/
	private String bundleNum;

	private ONLINE_STATUS onlineStatus = ONLINE_STATUS.OFFLINE;

	private ABLE_STATUS ableStatus = ABLE_STATUS.ABLE;

	/** 和第三方的同步状态 */
	private SYNC_STATUS syncStatus = SYNC_STATUS.ASYNC;

	private LockStatus lockStatus = LockStatus.IDLE;

	private Integer operateIndex = 0;

	private Integer operateCount = 0;
	
	/************************* 以下几个字段只对ipc设备有效 ********************************/
	/** 坐标-经度 */
	private Long longitude;

	/** 坐标-纬度 */
	private Long latitude;
	
	/** 流地址 */
	private String streamUrl;
	
	/************************* 以下几个字段只对大喇叭设备有效 ********************************/
	/** speaker标识 */
	private String identify;

	/************************* 以下几个字段只对合屏混音设备有效 ********************************/
	/** 音频解码路数最大值 */
	private Integer maxAudioSrcCnt;

	/** 视频解码路数最大值 */
	private Integer maxVideoSrcCnt;

	/** 剩余可用音频解码路数 */
	private Integer freeAudioSrcCnt;

	/** 剩余可用视频解码路数 */
	private Integer freeVideoSrcCnt;
	/*********************************************************/

	/** 当前设备账号登陆的设备标识ID(同一设备账号可能通过不同设备登陆，对应的设备标识ID也不同) */
	private String currentLoginId;

	// 设备来源（自身系统or外部系统，默认为自身系统）
	private SOURCE_TYPE sourceType = SOURCE_TYPE.SYSTEM;

	private String equipOrg;

	private String equipNode;

	private String equipFactInfo;

	/**
	 * TODO
	 */
	private String extraBindId = null;
	
	/** 设备所属用户id */
	private Long userId;
	
	public static String createBundleId() {
		return UUID.randomUUID().toString().replaceAll("-", "");
	}

	public static Boolean beDeviceBundle(String device_model) {
		return !"tvos".equals(device_model) && !"mobile".equals(device_model) && !"pc".equals(device_model);
	}

	public enum SOURCE_TYPE {
		SYSTEM, // 系统本身
		EXTERNAL; // 外部系统

		public static SOURCE_TYPE fromName(String name) {
			if (null == name) {
				return null;
			}
			switch (name) {
			case "SYSTEM":
				return SYSTEM;
			case "EXTERNAL":
				return EXTERNAL;
			default:
				return null;
			}
		}
	}

	public enum ONLINE_STATUS {
		ONLINE, OFFLINE
	}

	public enum ABLE_STATUS {
		ABLE, DISABLE
	}

	public enum SYNC_STATUS {
		SYNC, ASYNC
	}

	@Column(name = "bundle_id", unique = true)
	public String getBundleId() {
		return bundleId;
	}

	public void setBundleId(String bundleId) {
		this.bundleId = bundleId;
	}

	@Column(name = "online_password")
	public String getOnlinePassword() {
		return onlinePassword;
	}

	public void setOnlinePassword(String onlinePassword) {
		this.onlinePassword = onlinePassword;
	}

	@Column(name = "device_model")
	public String getDeviceModel() {
		return deviceModel;
	}

	public void setDeviceModel(String deviceModel) {
		this.deviceModel = deviceModel;
	}

	@Column(name = "device_version")
	public String getDeviceVersion() {
		return deviceVersion;
	}

	public void setDeviceVersion(String deviceVersion) {
		this.deviceVersion = deviceVersion;
	}

	@Column(name = "device_ip")
	public String getDeviceIp() {
		return deviceIp;
	}

	public void setDeviceIp(String deviceIp) {
		this.deviceIp = deviceIp;
	}

	@Column(name = "device_port")
	public Integer getDevicePort() {
		return devicePort;
	}

	public void setDevicePort(Integer devicePort) {
		this.devicePort = devicePort;
	}

	@Column(name = "access_node_uid")
	public String getAccessNodeUid() {
		return accessNodeUid;
	}

	public void setAccessNodeUid(String accessNodeUid) {
		this.accessNodeUid = accessNodeUid;
	}

	@Column(name = "online_status")
	@Enumerated(EnumType.STRING)
	public ONLINE_STATUS getOnlineStatus() {
		return onlineStatus;
	}

	public void setOnlineStatus(ONLINE_STATUS onlineStatus) {
		this.onlineStatus = onlineStatus;
	}

	@Column(name = "able_status")
	@Enumerated(EnumType.STRING)
	public ABLE_STATUS getAbleStatus() {
		return ableStatus;
	}

	public void setAbleStatus(ABLE_STATUS ableStatus) {
		this.ableStatus = ableStatus;
	}

	@Column(name = "sync_status")
	@Enumerated(EnumType.STRING)
	public SYNC_STATUS getSyncStatus() {
		return syncStatus;
	}

	public void setSyncStatus(SYNC_STATUS syncStatus) {
		this.syncStatus = syncStatus;
	}

	@Column(name = "source_type")
	@Enumerated(EnumType.STRING)
	public SOURCE_TYPE getSourceType() {
		return sourceType;
	}

	public void setSourceType(SOURCE_TYPE sourceType) {
		this.sourceType = sourceType;
	}

	@Column(name = "folder_id")
	public Long getFolderId() {
		return folderId;
	}

	public void setFolderId(Long folderId) {
		this.folderId = folderId;
	}

	@Column(name = "bundle_name")
	public String getBundleName() {
		return bundleName;
	}

	public void setBundleName(String bundleName) {
		this.bundleName = bundleName;
	}

	@Column(name = "user_name")
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@Column(name = "bundle_type")
	public String getBundleType() {
		return bundleType;
	}

	public void setBundleType(String bundleType) {
		this.bundleType = bundleType;
	}

	@Column
	public Integer getMaxAudioSrcCnt() {
		return maxAudioSrcCnt;
	}

	public void setMaxAudioSrcCnt(Integer maxAudioSrcCnt) {
		this.maxAudioSrcCnt = maxAudioSrcCnt;
	}

	@Column
	public Integer getMaxVideoSrcCnt() {
		return maxVideoSrcCnt;
	}

	public void setMaxVideoSrcCnt(Integer maxVideoSrcCnt) {
		this.maxVideoSrcCnt = maxVideoSrcCnt;
	}

	@Column
	public Integer getFreeAudioSrcCnt() {
		return freeAudioSrcCnt;
	}

	public void setFreeAudioSrcCnt(Integer freeAudioSrcCnt) {
		this.freeAudioSrcCnt = freeAudioSrcCnt;
	}

	@Column
	public Integer getFreeVideoSrcCnt() {
		return freeVideoSrcCnt;
	}

	public void setFreeVideoSrcCnt(Integer freeVideoSrcCnt) {
		this.freeVideoSrcCnt = freeVideoSrcCnt;
	}

	@Column
	@Enumerated(EnumType.STRING)
	public LockStatus getLockStatus() {
		return lockStatus;
	}

	public void setLockStatus(LockStatus lockStatus) {
		this.lockStatus = lockStatus;
	}

	@Column
	public Integer getOperateIndex() {
		return operateIndex;
	}

	public void setOperateIndex(Integer operateIndex) {
		this.operateIndex = operateIndex;
	}

	@Column
	public String getCurrentLoginId() {
		return currentLoginId;
	}

	public void setCurrentLoginId(String currentLoginId) {
		this.currentLoginId = currentLoginId;
	}

	@Column
	public String getBundleNum() {
		return bundleNum;
	}

	public void setBundleNum(String bundleNum) {
		this.bundleNum = bundleNum;
	}

	@Column
	public String getBundleAlias() {
		return bundleAlias;
	}

	public void setBundleAlias(String bundleAlias) {
		this.bundleAlias = bundleAlias;
	}

	@Column
	public Integer getOperateCount() {
		return operateCount;
	}

	public void setOperateCount(Integer operateCount) {
		this.operateCount = operateCount;
	}

	@Column
	public String getEquipOrg() {
		return equipOrg;
	}

	public void setEquipOrg(String equipOrg) {
		this.equipOrg = equipOrg;
	}

	@Column
	public String getEquipNode() {
		return equipNode;
	}

	public void setEquipNode(String equipNode) {
		this.equipNode = equipNode;
	}

	@Column
	public String getEquipFactInfo() {
		return equipFactInfo;
	}

	public void setEquipFactInfo(String equipFactInfo) {
		this.equipFactInfo = equipFactInfo;
	}

	public String getExtraBindId() {
		return extraBindId;
	}

	public void setExtraBindId(String extraBindId) {
		this.extraBindId = extraBindId;
	}

	public Integer getFolderIndex() {
		return folderIndex;
	}

	public void setFolderIndex(Integer folderIndex) {
		this.folderIndex = folderIndex;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getLongitude() {
		return longitude;
	}

	public void setLongitude(Long longitude) {
		this.longitude = longitude;
	}

	public Long getLatitude() {
		return latitude;
	}

	public void setLatitude(Long latitude) {
		this.latitude = latitude;
	}

	public String getStreamUrl() {
		return streamUrl;
	}

	public void setStreamUrl(String streamUrl) {
		this.streamUrl = streamUrl;
	}

	public String getIdentify() {
		return identify;
	}

	public void setIdentify(String identify) {
		this.identify = identify;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((ableStatus == null) ? 0 : ableStatus.hashCode());
		result = prime * result + ((accessNodeUid == null) ? 0 : accessNodeUid.hashCode());
		result = prime * result + ((bundleAlias == null) ? 0 : bundleAlias.hashCode());
		result = prime * result + ((bundleId == null) ? 0 : bundleId.hashCode());
		result = prime * result + ((bundleName == null) ? 0 : bundleName.hashCode());
		result = prime * result + ((bundleNum == null) ? 0 : bundleNum.hashCode());
		result = prime * result + ((bundleType == null) ? 0 : bundleType.hashCode());
		result = prime * result + ((currentLoginId == null) ? 0 : currentLoginId.hashCode());
		result = prime * result + ((deviceIp == null) ? 0 : deviceIp.hashCode());
		result = prime * result + ((deviceModel == null) ? 0 : deviceModel.hashCode());
		result = prime * result + ((devicePort == null) ? 0 : devicePort.hashCode());
		result = prime * result + ((deviceVersion == null) ? 0 : deviceVersion.hashCode());
		result = prime * result + ((equipFactInfo == null) ? 0 : equipFactInfo.hashCode());
		result = prime * result + ((equipNode == null) ? 0 : equipNode.hashCode());
		result = prime * result + ((equipOrg == null) ? 0 : equipOrg.hashCode());
		result = prime * result + ((extraBindId == null) ? 0 : extraBindId.hashCode());
		result = prime * result + ((folderId == null) ? 0 : folderId.hashCode());
		result = prime * result + ((folderIndex == null) ? 0 : folderIndex.hashCode());
		result = prime * result + ((freeAudioSrcCnt == null) ? 0 : freeAudioSrcCnt.hashCode());
		result = prime * result + ((freeVideoSrcCnt == null) ? 0 : freeVideoSrcCnt.hashCode());
		result = prime * result + ((lockStatus == null) ? 0 : lockStatus.hashCode());
		result = prime * result + ((maxAudioSrcCnt == null) ? 0 : maxAudioSrcCnt.hashCode());
		result = prime * result + ((maxVideoSrcCnt == null) ? 0 : maxVideoSrcCnt.hashCode());
		result = prime * result + ((onlinePassword == null) ? 0 : onlinePassword.hashCode());
		result = prime * result + ((onlineStatus == null) ? 0 : onlineStatus.hashCode());
		result = prime * result + ((operateCount == null) ? 0 : operateCount.hashCode());
		result = prime * result + ((operateIndex == null) ? 0 : operateIndex.hashCode());
		result = prime * result + ((sourceType == null) ? 0 : sourceType.hashCode());
		result = prime * result + ((syncStatus == null) ? 0 : syncStatus.hashCode());
		result = prime * result + ((username == null) ? 0 : username.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		BundlePO other = (BundlePO) obj;
		if (ableStatus != other.ableStatus)
			return false;
		if (accessNodeUid == null) {
			if (other.accessNodeUid != null)
				return false;
		} else if (!accessNodeUid.equals(other.accessNodeUid))
			return false;
		if (bundleAlias == null) {
			if (other.bundleAlias != null)
				return false;
		} else if (!bundleAlias.equals(other.bundleAlias))
			return false;
		if (bundleId == null) {
			if (other.bundleId != null)
				return false;
		} else if (!bundleId.equals(other.bundleId))
			return false;
		if (bundleName == null) {
			if (other.bundleName != null)
				return false;
		} else if (!bundleName.equals(other.bundleName))
			return false;
		if (bundleNum == null) {
			if (other.bundleNum != null)
				return false;
		} else if (!bundleNum.equals(other.bundleNum))
			return false;
		if (bundleType == null) {
			if (other.bundleType != null)
				return false;
		} else if (!bundleType.equals(other.bundleType))
			return false;
		if (currentLoginId == null) {
			if (other.currentLoginId != null)
				return false;
		} else if (!currentLoginId.equals(other.currentLoginId))
			return false;
		if (deviceIp == null) {
			if (other.deviceIp != null)
				return false;
		} else if (!deviceIp.equals(other.deviceIp))
			return false;
		if (deviceModel == null) {
			if (other.deviceModel != null)
				return false;
		} else if (!deviceModel.equals(other.deviceModel))
			return false;
		if (devicePort == null) {
			if (other.devicePort != null)
				return false;
		} else if (!devicePort.equals(other.devicePort))
			return false;
		if (deviceVersion == null) {
			if (other.deviceVersion != null)
				return false;
		} else if (!deviceVersion.equals(other.deviceVersion))
			return false;
		if (equipFactInfo == null) {
			if (other.equipFactInfo != null)
				return false;
		} else if (!equipFactInfo.equals(other.equipFactInfo))
			return false;
		if (equipNode == null) {
			if (other.equipNode != null)
				return false;
		} else if (!equipNode.equals(other.equipNode))
			return false;
		if (equipOrg == null) {
			if (other.equipOrg != null)
				return false;
		} else if (!equipOrg.equals(other.equipOrg))
			return false;
		if (extraBindId == null) {
			if (other.extraBindId != null)
				return false;
		} else if (!extraBindId.equals(other.extraBindId))
			return false;
		if (folderId == null) {
			if (other.folderId != null)
				return false;
		} else if (!folderId.equals(other.folderId))
			return false;
		if (folderIndex == null) {
			if (other.folderIndex != null)
				return false;
		} else if (!folderIndex.equals(other.folderIndex))
			return false;
		if (freeAudioSrcCnt == null) {
			if (other.freeAudioSrcCnt != null)
				return false;
		} else if (!freeAudioSrcCnt.equals(other.freeAudioSrcCnt))
			return false;
		if (freeVideoSrcCnt == null) {
			if (other.freeVideoSrcCnt != null)
				return false;
		} else if (!freeVideoSrcCnt.equals(other.freeVideoSrcCnt))
			return false;
		if (lockStatus != other.lockStatus)
			return false;
		if (maxAudioSrcCnt == null) {
			if (other.maxAudioSrcCnt != null)
				return false;
		} else if (!maxAudioSrcCnt.equals(other.maxAudioSrcCnt))
			return false;
		if (maxVideoSrcCnt == null) {
			if (other.maxVideoSrcCnt != null)
				return false;
		} else if (!maxVideoSrcCnt.equals(other.maxVideoSrcCnt))
			return false;
		if (onlinePassword == null) {
			if (other.onlinePassword != null)
				return false;
		} else if (!onlinePassword.equals(other.onlinePassword))
			return false;
		if (onlineStatus != other.onlineStatus)
			return false;
		if (operateCount == null) {
			if (other.operateCount != null)
				return false;
		} else if (!operateCount.equals(other.operateCount))
			return false;
		if (operateIndex == null) {
			if (other.operateIndex != null)
				return false;
		} else if (!operateIndex.equals(other.operateIndex))
			return false;
		if (sourceType != other.sourceType)
			return false;
		if (syncStatus != other.syncStatus)
			return false;
		if (username == null) {
			if (other.username != null)
				return false;
		} else if (!username.equals(other.username))
			return false;
		return true;
	}

}
