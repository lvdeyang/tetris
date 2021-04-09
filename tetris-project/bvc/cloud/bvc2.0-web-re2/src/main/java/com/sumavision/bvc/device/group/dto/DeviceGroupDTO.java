package com.sumavision.bvc.device.group.dto;

import java.util.Date;

import com.sumavision.bvc.device.group.enumeration.ForwardMode;
import com.sumavision.bvc.device.group.enumeration.GroupStatus;
import com.sumavision.bvc.device.group.enumeration.GroupType;
import com.sumavision.bvc.device.group.enumeration.TransmissionMode;

public class DeviceGroupDTO {

	private Long id;
	
	private String uuid;
	
	private Date updateTime;
	
	private String name;
	
	private Long userId; 
	
	private String userName;
	
	private TransmissionMode transmissionMode;
	
	private ForwardMode forwardMode;
	
	private GroupType type;
	
	private GroupStatus status;
	
	private Long avtplId;

	private String avtplName;
	
	private Long systemTplId;
	
	private String systemTplName;
	
	//组织，即BO的地区
    private String dicRegionId;
	
	private String dicRegionContent;
	
	//会议类型，即点播二级栏目
	private String dicProgramId;
	
	private String dicProgramContent;
	
	//直播栏目，可能改为自动选择
	private String dicCategoryLiveId;
	
	private String dicCategoryLiveContent;
	
	//存储位置，其code给CDN使用
	private String dicStorageLocationCode;
	
	private String dicStorageLocationContent;

	public DeviceGroupDTO(){}
	
	public DeviceGroupDTO(
			Long id, 
			String uuid,
			Date updateTime,
			String name,
			Long userId,
			String userName,
			TransmissionMode transmissionMode,
			ForwardMode forwardMode,
			GroupType type,
			GroupStatus status,
			Long avtplId,
			String avtplName,
			Long systemTplId,
			String systemTplName,
			String dicRegionId,
			String dicRegionContent,
			String dicProgramId,
			String dicProgramContent,
			String dicCategoryLiveId,
			String dicCategoryLiveContent,
			String dicStorageLocationCode,
			String dicStorageLocationContent){
		
		this.id = id;
		this.uuid = uuid;
		this.updateTime = updateTime;
		this.name = name;
		this.userId = userId;
		this.userName = userName;
		this.transmissionMode = transmissionMode;
		this.forwardMode = forwardMode;
		this.type = type;
		this.status = status;
		this.avtplId = avtplId;
		this.avtplName = avtplName;
		this.systemTplId = systemTplId;
		this.systemTplName = systemTplName;
		this.dicRegionId = dicRegionId;
		this.dicRegionContent = dicRegionContent;
		this.dicProgramId = dicProgramId;
		this.dicProgramContent = dicProgramContent;
		this.dicCategoryLiveId = dicCategoryLiveId;
		this.dicCategoryLiveContent = dicCategoryLiveContent;
		this.dicStorageLocationCode = dicStorageLocationCode;
		this.dicStorageLocationContent = dicStorageLocationContent;
		
	}
	
	public Long getId() {
		return id;
	}

	public DeviceGroupDTO setId(Long id) {
		this.id = id;
		return this;
	}

	public String getUuid() {
		return uuid;
	}

	public DeviceGroupDTO setUuid(String uuid) {
		this.uuid = uuid;
		return this;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public DeviceGroupDTO setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
		return this;
	}

	public String getName() {
		return name;
	}

	public DeviceGroupDTO setName(String name) {
		this.name = name;
		return this;
	}

	public Long getUserId() {
		return userId;
	}

	public DeviceGroupDTO setUserId(Long userId) {
		this.userId = userId;
		return this;
	}

	public String getUserName() {
		return userName;
	}

	public DeviceGroupDTO setUserName(String userName) {
		this.userName = userName;
		return this;
	}

	public TransmissionMode getTransmissionMode() {
		return transmissionMode;
	}

	public void setTransmissionMode(TransmissionMode transmissionMode) {
		this.transmissionMode = transmissionMode;
	}

	public ForwardMode getForwardMode() {
		return forwardMode;
	}

	public void setForwardMode(ForwardMode forwardMode) {
		this.forwardMode = forwardMode;
	}

	public GroupType getType() {
		return type;
	}

	public void setType(GroupType type) {
		this.type = type;
	}

	public GroupStatus getStatus() {
		return status;
	}

	public void setStatus(GroupStatus status) {
		this.status = status;
	}

	public Long getAvtplId() {
		return avtplId;
	}

	public DeviceGroupDTO setAvtplId(Long avtplId) {
		this.avtplId = avtplId;
		return this;
	}

	public String getAvtplName() {
		return avtplName;
	}

	public DeviceGroupDTO setAvtplName(String avtplName) {
		this.avtplName = avtplName;
		return this;
	}

	public Long getSystemTplId() {
		return systemTplId;
	}

	public DeviceGroupDTO setSystemTplId(Long systemTplId) {
		this.systemTplId = systemTplId;
		return this;
	}

	public String getSystemTplName() {
		return systemTplName;
	}

	public DeviceGroupDTO setSystemTplName(String systemTplName) {
		this.systemTplName = systemTplName;
		return this;
	}

	public String getDicRegionId() {
		return dicRegionId;
	}

	public DeviceGroupDTO setDicRegionId(String dicRegionId) {
		this.dicRegionId = dicRegionId;
		return this;
	}

	public String getDicRegionContent() {
		return dicRegionContent;
	}

	public DeviceGroupDTO setDicRegionContent(String dicRegionContent) {
		this.dicRegionContent = dicRegionContent;
		return this;
	}

	public String getDicProgramId() {
		return dicProgramId;
	}

	public DeviceGroupDTO setDicProgramId(String dicProgramId) {
		this.dicProgramId = dicProgramId;
		return this;
	}

	public String getDicProgramContent() {
		return dicProgramContent;
	}

	public DeviceGroupDTO setDicProgramContent(String dicProgramContent) {
		this.dicProgramContent = dicProgramContent;
		return this;
	}

	public String getDicCategoryLiveId() {
		return dicCategoryLiveId;
	}

	public DeviceGroupDTO setDicCategoryLiveId(String dicCategoryLiveId) {
		this.dicCategoryLiveId = dicCategoryLiveId;
		return this;
	}

	public String getDicCategoryLiveContent() {
		return dicCategoryLiveContent;
	}

	public DeviceGroupDTO setDicCategoryLiveContent(String dicCategoryLiveContent) {
		this.dicCategoryLiveContent = dicCategoryLiveContent;
		return this;
	}

	public String getDicStorageLocationCode() {
		return dicStorageLocationCode;
	}

	public void setDicStorageLocationCode(String dicStorageLocationCode) {
		this.dicStorageLocationCode = dicStorageLocationCode;
	}

	public String getDicStorageLocationContent() {
		return dicStorageLocationContent;
	}

	public void setDicStorageLocationContent(String dicStorageLocationContent) {
		this.dicStorageLocationContent = dicStorageLocationContent;
	}

}
