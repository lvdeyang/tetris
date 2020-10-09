package com.sumavision.tetris.mims.app.media.video;

public class MediaVideoTaskVO {
	private Long lastModified;
	
	private String name;
	
	private Long size;
	
	private String mimetype;
	
	private String type;
	
	private String icon;
	
	private String style;
	
	private Long folderId;

	private String folderName;
	
	private String folderNamePath;
	
	private String uploadStatus;
	
	private String previewUrl;
	
	private Integer progress;
	
	private Boolean synchro;
	
	public Boolean getSynchro() {
		return synchro;
	}

	public MediaVideoTaskVO setSynchro(Boolean synchro) {
		this.synchro = synchro;
		return this;
	}
	
	public Long getLastModified() {
		return lastModified;
	}

	public MediaVideoTaskVO setLastModified(Long lastModified) {
		this.lastModified = lastModified;
		return this;
	}

	public String getName() {
		return name;
	}

	public MediaVideoTaskVO setName(String name) {
		this.name = name;
		return this;
	}

	public Long getSize() {
		return size;
	}

	public MediaVideoTaskVO setSize(Long size) {
		this.size = size;
		return this;
	}

	public String getMimetype() {
		return mimetype;
	}

	public MediaVideoTaskVO setMimetype(String mimetype) {
		this.mimetype = mimetype;
		return this;
	}

	public String getIcon() {
		return icon;
	}

	public MediaVideoTaskVO setIcon(String icon) {
		this.icon = icon;
		return this;
	}

	public String getStyle() {
		return style;
	}

	public MediaVideoTaskVO setStyle(String style) {
		this.style = style;
		return this;
	}

	public String getType() {
		return type;
	}

	public MediaVideoTaskVO setType(String type) {
		this.type = type;
		return this;
	}

	public Long getFolderId() {
		return folderId;
	}

	public MediaVideoTaskVO setFolderId(Long folderId) {
		this.folderId = folderId;
		return this;
	}

	public String getFolderName() {
		return folderName;
	}

	public MediaVideoTaskVO setFolderName(String folderName) {
		this.folderName = folderName;
		return this;
	}

	public String getFolderNamePath() {
		return folderNamePath;
	}

	public MediaVideoTaskVO setFolderNamePath(String folderNamePath) {
		this.folderNamePath = folderNamePath;
		return this;
	}

	public String getUploadStatus() {
		return uploadStatus;
	}

	public MediaVideoTaskVO setUploadStatus(String uploadStatus) {
		this.uploadStatus = uploadStatus;
		return this;
	}

	public String getPreviewUrl() {
		return previewUrl;
	}

	public MediaVideoTaskVO setPreviewUrl(String previewUrl) {
		this.previewUrl = previewUrl;
		return this;
	}
	
	public Integer getProgress() {
		return progress;
	}

	public MediaVideoTaskVO setProgress(Integer progress) {
		this.progress = progress;
		return this;
	}
}
