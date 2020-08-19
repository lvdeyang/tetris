package com.sumavision.tetris.mims.app.media.picture;

import java.util.List;

import com.sumavision.tetris.mims.app.media.upload.MediaFileEquipmentPermissionPO;

public class MediaPictureVO{

	private Long id;
	
	private String uuid;
	
	private String updateTime;
	
	private String name;
	
	private String authorName;
	
	private String size;
	
	private String createTime;
	
	private String version;
	
	private String remarks;
	
	private List<String> tags;
	
	private List<String> keyWords;
	
	private String type;
	
	private String resourceType;
	
	private boolean removeable;
	
	private String icon;
	
	private String style;
	
	private String mimetype;
	
	private Integer progress;
	
	private String previewUrl;
	
	private String reviewStatus;
	
	private String processInstanceId;
	
	private Long folderId;
	
	private List<MediaFileEquipmentPermissionPO> deviceUpload;
	
	private List<MediaPictureVO> children;
	
	public Long getId() {
		return id;
	}

	public MediaPictureVO setId(Long id) {
		this.id = id;
		return this;
	}

	public String getUuid() {
		return uuid;
	}

	public MediaPictureVO setUuid(String uuid) {
		this.uuid = uuid;
		return this;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public MediaPictureVO setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
		return this;
	}

	public String getName() {
		return name;
	}

	public MediaPictureVO setName(String name) {
		this.name = name;
		return this;
	}

	public String getAuthorName() {
		return authorName;
	}

	public MediaPictureVO setAuthorName(String authorName) {
		this.authorName = authorName;
		return this;
	}

	public String getSize() {
		return size;
	}

	public MediaPictureVO setSize(String size) {
		this.size = size;
		return this;
	}

	public String getCreateTime() {
		return createTime;
	}

	public MediaPictureVO setCreateTime(String createTime) {
		this.createTime = createTime;
		return this;
	}

	public String getVersion() {
		return version;
	}

	public MediaPictureVO setVersion(String version) {
		this.version = version;
		return this;
	}

	public String getRemarks() {
		return remarks;
	}

	public MediaPictureVO setRemarks(String remarks) {
		this.remarks = remarks;
		return this;
	}

	public List<String> getTags() {
		return tags;
	}

	public MediaPictureVO setTags(List<String> tags) {
		this.tags = tags;
		return this;
	}

	public List<String> getKeyWords() {
		return keyWords;
	}

	public MediaPictureVO setKeyWords(List<String> keyWords) {
		this.keyWords = keyWords;
		return this;
	}
	
	public String getType() {
		return type;
	}

	public MediaPictureVO setType(String type) {
		this.type = type;
		return this;
	}

	public String getResourceType() {
		return resourceType;
	}

	public MediaPictureVO setResourceType(String resourceType) {
		this.resourceType = resourceType;
		return this;
	}

	public boolean isRemoveable() {
		return removeable;
	}

	public MediaPictureVO setRemoveable(boolean removeable) {
		this.removeable = removeable;
		return this;
	}

	public String getIcon() {
		return icon;
	}

	public MediaPictureVO setIcon(String icon) {
		this.icon = icon;
		return this;
	}

	public String getStyle() {
		return style;
	}

	public MediaPictureVO setStyle(String style) {
		this.style = style;
		return this;
	}
	
	public String getMimetype() {
		return mimetype;
	}

	public MediaPictureVO setMimetype(String mimetype) {
		this.mimetype = mimetype;
		return this;
	}

	public Integer getProgress() {
		return progress;
	}

	public MediaPictureVO setProgress(Integer progress) {
		this.progress = progress;
		return this;
	}
	
	public String getPreviewUrl() {
		return previewUrl;
	}

	public MediaPictureVO setPreviewUrl(String previewUrl) {
		this.previewUrl = previewUrl;
		return this;
	}

	public List<MediaPictureVO> getChildren() {
		return children;
	}

	public MediaPictureVO setChildren(List<MediaPictureVO> children) {
		this.children = children;
		return this;
	}

	public String getReviewStatus() {
		return reviewStatus;
	}

	public MediaPictureVO setReviewStatus(String reviewStatus) {
		this.reviewStatus = reviewStatus;
		return this;
	}

	public String getProcessInstanceId() {
		return processInstanceId;
	}

	public MediaPictureVO setProcessInstanceId(String processInstanceId) {
		this.processInstanceId = processInstanceId;
		return this;
	}

	public List<MediaFileEquipmentPermissionPO> getDeviceUpload() {
		return deviceUpload;
	}

	public MediaPictureVO setDeviceUpload(List<MediaFileEquipmentPermissionPO> deviceUpload) {
		this.deviceUpload = deviceUpload;
		return this;
	}

	public Long getFolderId() {
		return folderId;
	}

	public MediaPictureVO setFolderId(Long folderId) {
		this.folderId = folderId;
		return this;
	}

}
