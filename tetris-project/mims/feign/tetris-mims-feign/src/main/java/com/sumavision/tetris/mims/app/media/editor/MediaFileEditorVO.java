package com.sumavision.tetris.mims.app.media.editor;

import com.sumavision.tetris.mims.app.folder.FolderType;

public class MediaFileEditorVO {
	private Long mediaId;
	
	private FolderType mediaType;
	
	private String previewUrl;
	
	private String uploadTempPath;
	
	private String fileName;
	
	private Long size;
	
	private String duration;

	public Long getMediaId() {
		return mediaId;
	}

	public MediaFileEditorVO setMediaId(Long mediaId) {
		this.mediaId = mediaId;
		return this;
	}

	public FolderType getMediaType() {
		return mediaType;
	}

	public MediaFileEditorVO setMediaType(FolderType mediaType) {
		this.mediaType = mediaType;
		return this;
	}

	public String getPreviewUrl() {
		return previewUrl;
	}

	public MediaFileEditorVO setPreviewUrl(String previewUrl) {
		this.previewUrl = previewUrl;
		return this;
	}

	public String getUploadTempPath() {
		return uploadTempPath;
	}

	public MediaFileEditorVO setUploadTempPath(String uploadTempPath) {
		this.uploadTempPath = uploadTempPath;
		return this;
	}

	public String getFileName() {
		return fileName;
	}

	public MediaFileEditorVO setFileName(String fileName) {
		this.fileName = fileName;
		return this;
	}

	public Long getSize() {
		return size;
	}

	public MediaFileEditorVO setSize(Long size) {
		this.size = size;
		return this;
	}

	public String getDuration() {
		return duration;
	}

	public MediaFileEditorVO setDuration(String duration) {
		this.duration = duration;
		return this;
	}
}
