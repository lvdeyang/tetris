package com.sumavision.tetris.mims.app.media.avideo;

import java.util.List;

import com.sumavision.tetris.commons.util.wrapper.ArrayListWrapper;
import com.sumavision.tetris.mims.app.media.StoreType;

public class MediaAVideoVO {
	private Long id;
	
	private String uuid;

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
	
	private String duration;
	
	private List<MediaAVideoVO> children;
	
	public Long getId() {
		return id;
	}

	public MediaAVideoVO setId(Long id) {
		this.id = id;
		return this;
	}

	public String getUuid() {
		return uuid;
	}

	public MediaAVideoVO setUuid(String uuid) {
		this.uuid = uuid;
		return this;
	}

	public String getName() {
		return name;
	}

	public MediaAVideoVO setName(String name) {
		this.name = name;
		return this;
	}

	public String getAuthorName() {
		return authorName;
	}

	public MediaAVideoVO setAuthorName(String authorName) {
		this.authorName = authorName;
		return this;
	}

	public String getSize() {
		return size;
	}

	public MediaAVideoVO setSize(String size) {
		this.size = size;
		return this;
	}

	public String getCreateTime() {
		return createTime;
	}

	public MediaAVideoVO setCreateTime(String createTime) {
		this.createTime = createTime;
		return this;
	}

	public String getVersion() {
		return version;
	}

	public MediaAVideoVO setVersion(String version) {
		this.version = version;
		return this;
	}

	public String getRemarks() {
		return remarks;
	}

	public StoreType getStoreType() {
		return storeType;
	}

	public MediaAVideoVO setStoreType(StoreType storeType) {
		this.storeType = storeType;
		return this;
	}

	public String getUploadTmpPath() {
		return uploadTmpPath;
	}

	public MediaAVideoVO setUploadTmpPath(String uploadTmpPath) {
		this.uploadTmpPath = uploadTmpPath;
		return this;
	}

	public MediaAVideoVO setRemarks(String remarks) {
		this.remarks = remarks;
		return this;
	}

	public List<String> getTags() {
		return tags;
	}

	public MediaAVideoVO setTags(List<String> tags) {
		this.tags = tags;
		return this;
	}

	public List<String> getKeyWords() {
		return keyWords;
	}

	public MediaAVideoVO setKeyWords(List<String> keyWords) {
		this.keyWords = keyWords;
		return this;
	}
	
	public String getType() {
		return type;
	}

	public MediaAVideoVO setType(String type) {
		this.type = type;
		return this;
	}

	public String getIcon() {
		return icon;
	}

	public MediaAVideoVO setIcon(String icon) {
		this.icon = icon;
		return this;
	}

	public String getStyle() {
		return style;
	}

	public MediaAVideoVO setStyle(String style) {
		this.style = style;
		return this;
	}
	
	public String getMimetype() {
		return mimetype;
	}

	public MediaAVideoVO setMimetype(String mimetype) {
		this.mimetype = mimetype;
		return this;
	}

	public Integer getProgress() {
		return progress;
	}

	public MediaAVideoVO setProgress(Integer progress) {
		this.progress = progress;
		return this;
	}

	public String getPreviewUrl() {
		return previewUrl;
	}

	public MediaAVideoVO setPreviewUrl(String previewUrl) {
		this.previewUrl = previewUrl;
		return this;
	}

	public String getDuration() {
		return duration;
	}

	public MediaAVideoVO setDuration(String duration) {
		this.duration = duration;
		return this;
	}

	public List<MediaAVideoVO> getChildren() {
		return children;
	}

	public MediaAVideoVO setChildren(List<MediaAVideoVO> children) {
		this.children = children;
		return this;
	}
	
	public MediaAVideoVO cloneVO(){
		MediaAVideoVO newMedia = new MediaAVideoVO();
		newMedia.id = this.id;
		newMedia.uuid = this.uuid;
		newMedia.name = this.name;
		newMedia.previewUrl = this.previewUrl;
		newMedia.duration = this.duration;
		newMedia.children = new ArrayListWrapper<MediaAVideoVO>().getList();
		return newMedia;
	}
}
