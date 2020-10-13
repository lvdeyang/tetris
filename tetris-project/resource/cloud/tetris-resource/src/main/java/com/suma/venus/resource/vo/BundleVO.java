package com.suma.venus.resource.vo;

import com.alibaba.fastjson.JSONObject;
import com.suma.venus.resource.pojo.BundlePO;
import com.suma.venus.resource.pojo.BundlePO.SOURCE_TYPE;

public class BundleVO {
	
	/**操作*/
	private String operate;

	private Long id;
	
	private String bundleName;
	
	private String bundleAlias;
	
	private String username;
	
	private String bundleId;
	
	private String onlinePassword;
	
	private String deviceModel;
	
	private String bundleType;
	
	private String deviceVersion;
	
	private DeviceAddrVO deviceAddr;
	
	private String accessNodeUid;
	
	private String onlineStatus;
	
	private String ableStatus;
	
	private String lockStatus;
	
	private String bundleFolderId;
	
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
	
	public BundlePO toPO(){
		BundlePO po = new BundlePO();
		po.setBundleId(this.getBundleId());
		po.setBundleName(this.getBundleName());
		po.setBundleAlias(this.getBundleAlias());
		po.setUsername(this.getUsername());
		po.setOnlinePassword(this.getOnlinePassword());
		po.setDeviceModel(this.getDeviceModel());
		po.setBundleType(this.getBundleType());
		if(null != this.getDeviceAddr()){
			po.setDeviceIp(this.getDeviceAddr().getDeviceIp());
			po.setDevicePort(this.getDeviceAddr().getDevicePort());
		}
		po.setAccessNodeUid(this.getAccessNodeUid());
		po.setFolderId(this.getBundleFolderId() == null?null: Long.valueOf(this.getBundleFolderId()));
		po.setLongitude(this.getLongitude());
		po.setLatitude(this.getLatitude());
		po.setStreamUrl(this.getStreamUrl());
		po.setIdentify(this.getIdentify());
		po.setMulticastEncode(this.getMulticastEncode());
		po.setMulticastEncodeAddr(this.getMulticastEncodeAddr());
		po.setMulticastDecode(this.getMulticastDecode());
		po.setMulticastSourceIp(this.getMulticastSourceIp());
		po.setLocation(this.getLocation());
		po.setTranscod(this.getTranscod());
		return po;
	}
	
	public static BundleVO fromPO(BundlePO po){
		BundleVO vo = new BundleVO();
		vo.setId(po.getId());
		vo.setBundleId(po.getBundleId());
		vo.setBundleName(po.getBundleName());
		vo.setBundleAlias(po.getBundleAlias());
		vo.setUsername(po.getUsername());
		vo.setOnlinePassword(po.getOnlinePassword());
		vo.setDeviceModel(po.getDeviceModel());
		vo.setBundleType(po.getBundleType());
		vo.setAccessNodeUid(po.getAccessNodeUid());
		vo.setOnlineStatus(po.getOnlineStatus().toString());
		vo.setLockStatus(po.getLockStatus().toString());
		vo.setDeviceAddr(new DeviceAddrVO(po.getDeviceIp(),po.getDevicePort()));
		vo.setSourceType(po.getSourceType());
		vo.setBundleFolderId(po.getFolderId() == null? null: po.getFolderId().toString());
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
		return vo;
	}
	
	public enum CoderType{
		DEFAULT,
		ENCODER,
		DECODER
	}

	public String getOperate() {
		return operate;
	}

	public void setOperate(String operate) {
		this.operate = operate;
	}

	public JSONObject getParam() {
		return param;
	}

	public void setParam(JSONObject param) {
		this.param = param;
	}

	public String getBundleFolderName() {
		return bundleFolderName;
	}

	public void setBundleFolderName(String bundleFolderName) {
		this.bundleFolderName = bundleFolderName;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getMulticastSourceIp() {
		return multicastSourceIp;
	}

	public void setMulticastSourceIp(String multicastSourceIp) {
		this.multicastSourceIp = multicastSourceIp;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getBundleId() {
		return bundleId;
	}

	public void setBundleId(String bundleId) {
		this.bundleId = bundleId;
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

	public String getOnlineStatus() {
		return onlineStatus;
	}

	public void setOnlineStatus(String onlineStatus) {
		this.onlineStatus = onlineStatus;
	}

	public String getAbleStatus() {
		return ableStatus;
	}

	public void setAbleStatus(String ableStatus) {
		this.ableStatus = ableStatus;
	}

	public String getAccessNodeUid() {
		return accessNodeUid;
	}

	public void setAccessNodeUid(String accessNodeUid) {
		this.accessNodeUid = accessNodeUid;
	}

	public String getBundleName() {
		return bundleName;
	}

	public void setBundleName(String bundleName) {
		this.bundleName = bundleName;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getBundleType() {
		return bundleType;
	}

	public void setBundleType(String bundleType) {
		this.bundleType = bundleType;
	}

	public String getLockStatus() {
		return lockStatus;
	}

	public void setLockStatus(String lockStatus) {
		this.lockStatus = lockStatus;
	}

	public String getBundleAlias() {
		return bundleAlias;
	}

	public void setBundleAlias(String bundleAlias) {
		this.bundleAlias = bundleAlias;
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

	public String getBundleFolderId() {
		return bundleFolderId;
	}

	public void setBundleFolderId(String bundleFolderId) {
		this.bundleFolderId = bundleFolderId;
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

	public Boolean getTranscod() {
		return transcod;
	}

	public void setTranscod(Boolean transcod) {
		this.transcod = transcod;
	}
	
}
