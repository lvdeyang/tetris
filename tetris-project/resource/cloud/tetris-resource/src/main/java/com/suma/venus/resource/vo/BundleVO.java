package com.suma.venus.resource.vo;

import com.suma.venus.resource.pojo.BundlePO;
import com.suma.venus.resource.pojo.BundlePO.SOURCE_TYPE;

public class BundleVO {

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
	private Long longitude;

	/** 坐标-纬度 */
	private Long latitude;
	
	/** 流地址 */
	private String streamUrl;
	
	/** 标识 */
	private String identify;
	
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
		return vo;
	}
	
	public enum CoderType{
		DEFAULT,
		ENCODER,
		DECODER
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
	
}
