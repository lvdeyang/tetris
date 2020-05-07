package com.sumavision.tetris.mims.app.media.stream.audio;

import java.util.List;

public class MediaAudioStreamVO {

	private Long id;
	
	private String uuid;
	
	private String updateTime;
	
	private String previewUrl;
	
	private String name;
	
	private String authorName;
	
	private String createTime;
	
	private String remarks;
	
	private List<String> tags;
	
	private List<String> keyWords;
	
	private String type;
	
	private boolean removeable;
	
	private String icon;
	
	private String style;
	
	private String reviewStatus;
	
	private String processInstanceId;
	
	private List<MediaAudioStreamVO> children;
	
	public Long getId() {
		return id;
	}

	public MediaAudioStreamVO setId(Long id) {
		this.id = id;
		return this;
	}

	public String getUuid() {
		return uuid;
	}

	public MediaAudioStreamVO setUuid(String uuid) {
		this.uuid = uuid;
		return this;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public MediaAudioStreamVO setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
		return this;
	}

	public String getPreviewUrl() {
		return previewUrl;
	}

	public MediaAudioStreamVO setPreviewUrl(String previewUrl) {
		this.previewUrl = previewUrl;
		return this;
	}

	public String getName() {
		return name;
	}

	public MediaAudioStreamVO setName(String name) {
		this.name = name;
		return this;
	}

	public String getAuthorName() {
		return authorName;
	}

	public MediaAudioStreamVO setAuthorName(String authorName) {
		this.authorName = authorName;
		return this;
	}

	public String getCreateTime() {
		return createTime;
	}

	public MediaAudioStreamVO setCreateTime(String createTime) {
		this.createTime = createTime;
		return this;
	}

	public String getRemarks() {
		return remarks;
	}

	public MediaAudioStreamVO setRemarks(String remarks) {
		this.remarks = remarks;
		return this;
	}

	public List<String> getTags() {
		return tags;
	}

	public MediaAudioStreamVO setTags(List<String> tags) {
		this.tags = tags;
		return this;
	}

	public List<String> getKeyWords() {
		return keyWords;
	}

	public MediaAudioStreamVO setKeyWords(List<String> keyWords) {
		this.keyWords = keyWords;
		return this;
	}
	
	public String getType() {
		return type;
	}

	public MediaAudioStreamVO setType(String type) {
		this.type = type;
		return this;
	}

	public boolean isRemoveable() {
		return removeable;
	}

	public MediaAudioStreamVO setRemoveable(boolean removeable) {
		this.removeable = removeable;
		return this;
	}

	public String getIcon() {
		return icon;
	}

	public MediaAudioStreamVO setIcon(String icon) {
		this.icon = icon;
		return this;
	}

	public String getStyle() {
		return style;
	}

	public MediaAudioStreamVO setStyle(String style) {
		this.style = style;
		return this;
	}
	
	public String getReviewStatus() {
		return reviewStatus;
	}

	public MediaAudioStreamVO setReviewStatus(String reviewStatus) {
		this.reviewStatus = reviewStatus;
		return this;
	}

	public String getProcessInstanceId() {
		return processInstanceId;
	}

	public MediaAudioStreamVO setProcessInstanceId(String processInstanceId) {
		this.processInstanceId = processInstanceId;
		return this;
	}

	public List<MediaAudioStreamVO> getChildren() {
		return children;
	}

	public MediaAudioStreamVO setChildren(List<MediaAudioStreamVO> children) {
		this.children = children;
		return this;
	}
}
