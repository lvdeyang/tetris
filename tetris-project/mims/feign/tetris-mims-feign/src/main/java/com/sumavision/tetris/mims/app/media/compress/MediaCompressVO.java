package com.sumavision.tetris.mims.app.media.compress;

import java.util.List;

public class MediaCompressVO {

	private String name;
	
	private String authorName;
	
	private String size;
	
	private String createTime;
	
	private String version;
	
	private String remarks;
	
	private List<String> tags;
	
	private List<String> keyWords;
	
	private String type;
	
	private String icon;
	
	private String style;
	
	private String mimetype;
	
	private Integer progress;
	
	private String previewUrl;
	
	private String uploadTmpPath;
	
	public String getName() {
		return name;
	}

	public MediaCompressVO setName(String name) {
		this.name = name;
		return this;
	}

	public String getAuthorName() {
		return authorName;
	}

	public MediaCompressVO setAuthorName(String authorName) {
		this.authorName = authorName;
		return this;
	}

	public String getSize() {
		return size;
	}

	public MediaCompressVO setSize(String size) {
		this.size = size;
		return this;
	}

	public String getCreateTime() {
		return createTime;
	}

	public MediaCompressVO setCreateTime(String createTime) {
		this.createTime = createTime;
		return this;
	}

	public String getVersion() {
		return version;
	}

	public MediaCompressVO setVersion(String version) {
		this.version = version;
		return this;
	}

	public String getRemarks() {
		return remarks;
	}

	public MediaCompressVO setRemarks(String remarks) {
		this.remarks = remarks;
		return this;
	}

	public List<String> getTags() {
		return tags;
	}

	public MediaCompressVO setTags(List<String> tags) {
		this.tags = tags;
		return this;
	}

	public List<String> getKeyWords() {
		return keyWords;
	}

	public MediaCompressVO setKeyWords(List<String> keyWords) {
		this.keyWords = keyWords;
		return this;
	}
	
	public String getType() {
		return type;
	}

	public MediaCompressVO setType(String type) {
		this.type = type;
		return this;
	}

	public String getIcon() {
		return icon;
	}

	public MediaCompressVO setIcon(String icon) {
		this.icon = icon;
		return this;
	}

	public String getStyle() {
		return style;
	}

	public MediaCompressVO setStyle(String style) {
		this.style = style;
		return this;
	}
	
	public String getMimetype() {
		return mimetype;
	}

	public MediaCompressVO setMimetype(String mimetype) {
		this.mimetype = mimetype;
		return this;
	}

	public Integer getProgress() {
		return progress;
	}

	public MediaCompressVO setProgress(Integer progress) {
		this.progress = progress;
		return this;
	}

	public String getPreviewUrl() {
		return previewUrl;
	}

	public MediaCompressVO setPreviewUrl(String previewUrl) {
		this.previewUrl = previewUrl;
		return this;
	}

	public String getUploadTmpPath() {
		return uploadTmpPath;
	}

	public MediaCompressVO setUploadTmpPath(String uploadTmpPath) {
		this.uploadTmpPath = uploadTmpPath;
		return this;
	}

}
