package com.sumavision.tetris.mims.app.media.stream.video;

import java.util.List;

public class MediaVideoStreamVO {
	private Long id;
	
	private String uuid;
	
	private String updateTime;
	
	private List<String> previewUrl;

	private String name;
	
	private String authorName;
	
	private String createTime;
	
	private String remarks;
	
	private List<String> tags;
	
	private List<String> keyWords;
	
	private String type;
	
	private String icon;
	
	private String style;
	
	private List<MediaVideoStreamVO> children;
	
	public Long getId() {
		return id;
	}

	public MediaVideoStreamVO setId(Long id) {
		this.id = id;
		return this;
	}
	
	public String getUuid() {
		return uuid;
	}

	public MediaVideoStreamVO setUuid(String uuid) {
		this.uuid = uuid;
		return this;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public MediaVideoStreamVO setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
		return this;
	}

	public List<String> getPreviewUrl() {
		return previewUrl;
	}

	public MediaVideoStreamVO setPreviewUrl(List<String> previewUrl) {
		this.previewUrl = previewUrl;
		return this;
	}

	public String getName() {
		return name;
	}

	public MediaVideoStreamVO setName(String name) {
		this.name = name;
		return this;
	}

	public String getAuthorName() {
		return authorName;
	}

	public MediaVideoStreamVO setAuthorName(String authorName) {
		this.authorName = authorName;
		return this;
	}

	public String getCreateTime() {
		return createTime;
	}

	public MediaVideoStreamVO setCreateTime(String createTime) {
		this.createTime = createTime;
		return this;
	}

	public String getRemarks() {
		return remarks;
	}

	public MediaVideoStreamVO setRemarks(String remarks) {
		this.remarks = remarks;
		return this;
	}

	public List<String> getTags() {
		return tags;
	}

	public MediaVideoStreamVO setTags(List<String> tags) {
		this.tags = tags;
		return this;
	}

	public List<String> getKeyWords() {
		return keyWords;
	}

	public MediaVideoStreamVO setKeyWords(List<String> keyWords) {
		this.keyWords = keyWords;
		return this;
	}
	
	public String getType() {
		return type;
	}

	public MediaVideoStreamVO setType(String type) {
		this.type = type;
		return this;
	}

	public String getIcon() {
		return icon;
	}

	public MediaVideoStreamVO setIcon(String icon) {
		this.icon = icon;
		return this;
	}

	public String getStyle() {
		return style;
	}

	public MediaVideoStreamVO setStyle(String style) {
		this.style = style;
		return this;
	}

	public List<MediaVideoStreamVO> getChildren() {
		return children;
	}

	public MediaVideoStreamVO setChildren(List<MediaVideoStreamVO> children) {
		this.children = children;
		return this;
	}
}
