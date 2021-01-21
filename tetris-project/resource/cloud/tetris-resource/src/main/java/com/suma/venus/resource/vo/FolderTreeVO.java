package com.suma.venus.resource.vo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.suma.venus.resource.pojo.BundlePO;
import com.suma.venus.resource.pojo.BundlePO.CoderType;
import com.suma.venus.resource.pojo.BundlePO.ONLINE_STATUS;
import com.suma.venus.resource.pojo.BundlePO.SOURCE_TYPE;

/**
 * 分组树VO
 * 
 * @author lxw
 * 
 * @author chenmo
 *
 */
public class FolderTreeVO {

	/** 节点ID */
	private Long id;

	/** 父节点ID */
	private Long parentId;

	/** 节点显示名称 */
	private String name;

	/** 是否文件夹节点 */
	@Deprecated
	private boolean beFolder;

	/** 标记文件夹节点是否上传ldap **/
	private Boolean toLdap;

	/** 设备Uid，当beFolder为false时有效，设备节点时有效 */
	private String bundleId;

	/** 用户名，当beFolder为false时有效，用户节点时有效 **/
	private String username;

	/** 文件夹的类型 **/
	private String folderType;

	/** 子节点集合 */
	private List<FolderTreeVO> children;

	/** trur表示本系统的数据，false来自外部同步的数据 **/
	private Boolean systemSourceType;

	private Integer folderIndex;

	private String nodeType;
	
	private ONLINE_STATUS onlineStatus;
	
	//------------------------------------
	
	private String bundleName;
	
	private String bundleAlias;
	
	private String onlinePassword;
	
	private String deviceModel;
	
	private String bundleType;
	
	private String deviceVersion;
	
	private DeviceAddrVO deviceAddr;
	
	private String accessNodeUid;
	
	private String ableStatus;
	
	private String lockStatus;
	
	private CoderType coderType;
	
	private SOURCE_TYPE sourceType;
	
	/** 坐标-经度 */
	private String longitude;

	/** 坐标-纬度 */
	private String latitude;
	
	/** 流地址 */
	private String streamUrl;
	
	/** 标识 */
	private String identify;
	
	/** 是否开启组播编码 */
	private Boolean multicastEncode;
	
	/** 组播编码地址 */
	private String multicastEncodeAddr;
	
	/** 是否开启组播解码 */
	private Boolean multicastDecode;
	
	/** 组播源ip */
	private String multicastSourceIp;
	
	/**地点*/
	private String location;
	
	/**设备folderName*/
	private String bundleFolderName;
	
	/**是否转码*/
	private Boolean transcod;
	
	/**扩展字段**/
	private JSONObject param;
	
	/** 所属服务节点*/
	private String equipFactInfo;
	
	//----------------------------------
	
	public String getBundleName() {
		return bundleName;
	}

	public void setBundleName(String bundleName) {
		this.bundleName = bundleName;
	}

	public String getBundleAlias() {
		return bundleAlias;
	}

	public void setBundleAlias(String bundleAlias) {
		this.bundleAlias = bundleAlias;
	}

	public String getOnlinePassword() {
		return onlinePassword;
	}

	public void setOnlinePassword(String onlinePassword) {
		this.onlinePassword = onlinePassword;
	}

	public String getDeviceModel() {
		return deviceModel;
	}

	public void setDeviceModel(String deviceModel) {
		this.deviceModel = deviceModel;
	}

	public String getBundleType() {
		return bundleType;
	}

	public void setBundleType(String bundleType) {
		this.bundleType = bundleType;
	}

	public String getDeviceVersion() {
		return deviceVersion;
	}

	public void setDeviceVersion(String deviceVersion) {
		this.deviceVersion = deviceVersion;
	}

	public DeviceAddrVO getDeviceAddr() {
		return deviceAddr;
	}

	public void setDeviceAddr(DeviceAddrVO deviceAddr) {
		this.deviceAddr = deviceAddr;
	}

	public String getAccessNodeUid() {
		return accessNodeUid;
	}

	public void setAccessNodeUid(String accessNodeUid) {
		this.accessNodeUid = accessNodeUid;
	}

	public String getAbleStatus() {
		return ableStatus;
	}

	public void setAbleStatus(String ableStatus) {
		this.ableStatus = ableStatus;
	}

	public String getLockStatus() {
		return lockStatus;
	}

	public void setLockStatus(String lockStatus) {
		this.lockStatus = lockStatus;
	}

	public CoderType getCoderType() {
		return coderType;
	}

	public void setCoderType(CoderType coderType) {
		this.coderType = coderType;
	}

	public SOURCE_TYPE getSourceType() {
		return sourceType;
	}

	public void setSourceType(SOURCE_TYPE sourceType) {
		this.sourceType = sourceType;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
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

	public Boolean getMulticastEncode() {
		return multicastEncode;
	}

	public void setMulticastEncode(Boolean multicastEncode) {
		this.multicastEncode = multicastEncode;
	}

	public String getMulticastEncodeAddr() {
		return multicastEncodeAddr;
	}

	public void setMulticastEncodeAddr(String multicastEncodeAddr) {
		this.multicastEncodeAddr = multicastEncodeAddr;
	}

	public Boolean getMulticastDecode() {
		return multicastDecode;
	}

	public void setMulticastDecode(Boolean multicastDecode) {
		this.multicastDecode = multicastDecode;
	}

	public String getMulticastSourceIp() {
		return multicastSourceIp;
	}

	public void setMulticastSourceIp(String multicastSourceIp) {
		this.multicastSourceIp = multicastSourceIp;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getBundleFolderName() {
		return bundleFolderName;
	}

	public void setBundleFolderName(String bundleFolderName) {
		this.bundleFolderName = bundleFolderName;
	}

	public Boolean getTranscod() {
		return transcod;
	}

	public void setTranscod(Boolean transcod) {
		this.transcod = transcod;
	}

	public JSONObject getParam() {
		return param;
	}

	public void setParam(JSONObject param) {
		this.param = param;
	}

	public String getEquipFactInfo() {
		return equipFactInfo;
	}

	public void setEquipFactInfo(String equipFactInfo) {
		this.equipFactInfo = equipFactInfo;
	}

	public static FolderTreeVO fromBundlePO(BundlePO po){
		FolderTreeVO vo = new FolderTreeVO();
		vo.setBundleName(po.getBundleName());
		vo.setBundleAlias(po.getBundleAlias());
		vo.setUsername(po.getUsername());
		vo.setOnlinePassword(po.getOnlinePassword());
		vo.setDeviceModel(po.getDeviceModel());
		vo.setBundleType(po.getBundleType());
		vo.setAccessNodeUid(po.getAccessNodeUid());
		vo.setLockStatus(po.getLockStatus().toString());
		vo.setDeviceAddr(new DeviceAddrVO(po.getDeviceIp(),po.getDevicePort()));
		vo.setSourceType(po.getSourceType());
		vo.setLongitude(po.getLongitude());
		vo.setLatitude(po.getLatitude());
		vo.setStreamUrl(po.getStreamUrl());
		vo.setIdentify(po.getIdentify());
		vo.setMulticastEncode(po.getMulticastEncode()==null?false:po.getMulticastEncode());
		vo.setMulticastEncodeAddr(po.getMulticastEncodeAddr()==null?"":po.getMulticastEncodeAddr());
		vo.setMulticastDecode(po.getMulticastDecode()==null?false:po.getMulticastDecode());
		vo.setMulticastSourceIp(po.getMulticastSourceIp()==null?"":po.getMulticastSourceIp());
		vo.setLocation(po.getLocation()==null?"":po.getLocation());
		vo.setTranscod(po.getTranscod()==null?false:po.getTranscod());
		vo.setCoderType(po.getCoderType()==null?null:po.getCoderType());
		vo.setEquipFactInfo(po.getEquipFactInfo());
		return vo;
		
	}
	//----------------------------------
	
	public ONLINE_STATUS getOnlineStatus() {
		return onlineStatus;
	}

	public void setOnlineStatus(ONLINE_STATUS onlineStatus) {
		this.onlineStatus = onlineStatus;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<FolderTreeVO> getChildren() {
		return children;
	}

	public void setChildren(List<FolderTreeVO> children) {
		this.children = children;
	}

	public boolean isBeFolder() {
		return beFolder;
	}

	public void setBeFolder(boolean beFolder) {
		this.beFolder = beFolder;
	}

	public String getBundleId() {
		return bundleId;
	}

	public void setBundleId(String bundleId) {
		this.bundleId = bundleId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Boolean getToLdap() {
		return toLdap;
	}

	public void setToLdap(Boolean toLdap) {
		this.toLdap = toLdap;
	}

	public Boolean getSystemSourceType() {
		return systemSourceType;
	}

	public void setSystemSourceType(Boolean systemSourceType) {
		this.systemSourceType = systemSourceType;
	}

	public Integer getFolderIndex() {
		return folderIndex;
	}

	public void setFolderIndex(Integer folderIndex) {
		this.folderIndex = folderIndex;
	}

	public String getNodeType() {
		return nodeType;
	}

	public void setNodeType(String nodeType) {
		this.nodeType = nodeType;
	}

	public String getFolderType() {
		return folderType;
	}

	public void setFolderType(String folderType) {
		this.folderType = folderType;
	}

	public static class FolderIndecComparator implements Comparator<FolderTreeVO>{

		@Override
		public int compare(FolderTreeVO o1, FolderTreeVO o2) {
			// TODO Auto-generated method stub
			if (o1.getFolderIndex() == null && o2.getFolderIndex()==null) {
				return o1.getName().compareTo(o2.getName());
			}else if (o1.getFolderIndex()==null && o2.getFolderIndex()!=null ){
				return -1;
			}else if (o1.getFolderIndex() != null&&o2.getFolderIndex() == null) {
				return 1;
			}else {
				return o1.getFolderIndex() - o2.getFolderIndex();
			}
		}
		 
	}
	
}
