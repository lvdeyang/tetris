package com.sumavision.tetris.mims.app.media.txt;

import java.util.List;

public class MediaTxtVO {
	private Long id;
	
	private String uuid;
	
	private String updateTime;
	
	private String content;
	
	private String name;
	
	private String authorName;
	
	private String createTime;
	
	private String remarks;
	
	private List<String> tags;
	
	private List<String> keyWords;
	
	private String type;
	
	private String resourceType;
	
	private boolean removeable;
	
	private String icon;
	
	private String style;
	
	private String previewUrl;
	
	private String uploadTmpPath;
	
	private String reviewStatus;
	
	private String processInstanceId;
	
	private Long size;
	
	private Integer progress;
	
	private String addition;
	
	private List<MediaTxtVO> children;
	
	public Long getId() {
		return id;
	}

	public MediaTxtVO setId(Long id) {
		this.id = id;
		return this;
	}

	public String getUuid() {
		return uuid;
	}

	public MediaTxtVO setUuid(String uuid) {
		this.uuid = uuid;
		return this;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public MediaTxtVO setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
		return this;
	}

	public String getContent() {
		return content;
	}

	public MediaTxtVO setContent(String content) {
		this.content = content;
		return this;
	}

	public String getName() {
		return name;
	}

	public MediaTxtVO setName(String name) {
		this.name = name;
		return this;
	}

	public String getAuthorName() {
		return authorName;
	}

	public MediaTxtVO setAuthorName(String authorName) {
		this.authorName = authorName;
		return this;
	}

	public String getCreateTime() {
		return createTime;
	}

	public MediaTxtVO setCreateTime(String createTime) {
		this.createTime = createTime;
		return this;
	}

	public String getRemarks() {
		return remarks;
	}

	public MediaTxtVO setRemarks(String remarks) {
		this.remarks = remarks;
		return this;
	}

	public List<String> getTags() {
		return tags;
	}

	public MediaTxtVO setTags(List<String> tags) {
		this.tags = tags;
		return this;
	}

	public List<String> getKeyWords() {
		return keyWords;
	}

	public MediaTxtVO setKeyWords(List<String> keyWords) {
		this.keyWords = keyWords;
		return this;
	}
	
	public String getType() {
		return type;
	}

	public MediaTxtVO setType(String type) {
		this.type = type;
		return this;
	}

	public String getResourceType() {
		return resourceType;
	}

	public MediaTxtVO setResourceType(String resourceType) {
		this.resourceType = resourceType;
		return this;
	}

	public boolean isRemoveable() {
		return removeable;
	}

	public MediaTxtVO setRemoveable(boolean removeable) {
		this.removeable = removeable;
		return this;
	}

	public String getIcon() {
		return icon;
	}

	public MediaTxtVO setIcon(String icon) {
		this.icon = icon;
		return this;
	}

	public String getStyle() {
		return style;
	}

	public MediaTxtVO setStyle(String style) {
		this.style = style;
		return this;
	}
	
	public String getPreviewUrl() {
		return previewUrl;
	}

	public MediaTxtVO setPreviewUrl(String previewUrl) {
		this.previewUrl = previewUrl;
		return this;
	}
	
	public String getUploadTmpPath() {
		return uploadTmpPath;
	}

	public MediaTxtVO setUploadTmpPath(String uploadTmpPath) {
		this.uploadTmpPath = uploadTmpPath;
		return this;
	}

	public String getReviewStatus() {
		return reviewStatus;
	}

	public MediaTxtVO setReviewStatus(String reviewStatus) {
		this.reviewStatus = reviewStatus;
		return this;
	}

	public String getProcessInstanceId() {
		return processInstanceId;
	}

	public MediaTxtVO setProcessInstanceId(String processInstanceId) {
		this.processInstanceId = processInstanceId;
		return this;
	}

	public Long getSize() {
		return size;
	}

	public MediaTxtVO setSize(Long size) {
		this.size = size;
		return this;
	}

	public Integer getProgress() {
		return progress;
	}

	public MediaTxtVO setProgress(Integer progress) {
		this.progress = progress;
		return this;
	}

	public String getAddition() {
		return addition;
	}

	public MediaTxtVO setAddition(String addition) {
		this.addition = addition;
		return this;
	}

	public List<MediaTxtVO> getChildren() {
		return children;
	}

	public void setChildren(List<MediaTxtVO> children) {
		this.children = children;
	}
}
