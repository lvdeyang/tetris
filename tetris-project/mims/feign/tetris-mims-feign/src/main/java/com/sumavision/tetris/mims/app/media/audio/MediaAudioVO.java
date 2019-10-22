package com.sumavision.tetris.mims.app.media.audio;

import java.util.List;

import com.sumavision.tetris.mims.app.media.StoreType;

public class MediaAudioVO {

	private String name;
	
	private String authorName;
	
	private String size;
	
	private String createTime;
	
	private String version;
	
	private String remarks;
	
	private StoreType storeType;
	
	private String uploadTmpPath;
	
	private List<String> tags;
	
	private List<String> keyWords;
	
	private String type;
	
	private String icon;
	
	private String style;
	
	private String mimetype;
	
	private Integer progress;
	
	private String previewUrl;
	
	private Integer hotWeight;
	
	private List<MediaAudioVO> children;
	
	public String getName() {
		return name;
	}

	public MediaAudioVO setName(String name) {
		this.name = name;
		return this;
	}

	public String getAuthorName() {
		return authorName;
	}

	public MediaAudioVO setAuthorName(String authorName) {
		this.authorName = authorName;
		return this;
	}

	public String getSize() {
		return size;
	}

	public MediaAudioVO setSize(String size) {
		this.size = size;
		return this;
	}

	public String getCreateTime() {
		return createTime;
	}

	public MediaAudioVO setCreateTime(String createTime) {
		this.createTime = createTime;
		return this;
	}

	public String getVersion() {
		return version;
	}

	public MediaAudioVO setVersion(String version) {
		this.version = version;
		return this;
	}

	public String getRemarks() {
		return remarks;
	}

	public MediaAudioVO setRemarks(String remarks) {
		this.remarks = remarks;
		return this;
	}

	public StoreType getStoreType() {
		return storeType;
	}

	public MediaAudioVO setStoreType(StoreType storeType) {
		this.storeType = storeType;
		return this;
	}

	public String getUploadTmpPath() {
		return uploadTmpPath;
	}

	public MediaAudioVO setUploadTmpPath(String uploadTmpPath) {
		this.uploadTmpPath = uploadTmpPath;
		return this;
	}

	public List<String> getTags() {
		return tags;
	}

	public MediaAudioVO setTags(List<String> tags) {
		this.tags = tags;
		return this;
	}

	public List<String> getKeyWords() {
		return keyWords;
	}

	public MediaAudioVO setKeyWords(List<String> keyWords) {
		this.keyWords = keyWords;
		return this;
	}
	
	public String getType() {
		return type;
	}

	public MediaAudioVO setType(String type) {
		this.type = type;
		return this;
	}

	public String getIcon() {
		return icon;
	}

	public MediaAudioVO setIcon(String icon) {
		this.icon = icon;
		return this;
	}

	public String getStyle() {
		return style;
	}

	public MediaAudioVO setStyle(String style) {
		this.style = style;
		return this;
	}
	
	public String getMimetype() {
		return mimetype;
	}

	public MediaAudioVO setMimetype(String mimetype) {
		this.mimetype = mimetype;
		return this;
	}

	public Integer getProgress() {
		return progress;
	}

	public MediaAudioVO setProgress(Integer progress) {
		this.progress = progress;
		return this;
	}
	
	public String getPreviewUrl() {
		return previewUrl;
	}

	public MediaAudioVO setPreviewUrl(String previewUrl) {
		this.previewUrl = previewUrl;
		return this;
	}

	public Integer getHotWeight() {
		return hotWeight;
	}

	public MediaAudioVO setHotWeight(Integer hotWeight) {
		this.hotWeight = hotWeight;
		return this;
	}

	public List<MediaAudioVO> getChildren() {
		return children;
	}

	public MediaAudioVO setChildren(List<MediaAudioVO> children) {
		this.children = children;
		return this;
	}

}
