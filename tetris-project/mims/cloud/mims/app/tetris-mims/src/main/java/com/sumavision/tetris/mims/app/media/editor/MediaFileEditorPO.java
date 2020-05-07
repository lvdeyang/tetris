package com.sumavision.tetris.mims.app.media.editor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.sumavision.tetris.mims.app.folder.FolderType;
import com.sumavision.tetris.orm.po.AbstractBasePO;

@Entity
@Table(name = "MIMS_MEDIA_EDITOR_PERMISSION")
public class MediaFileEditorPO extends AbstractBasePO{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** 媒资id */
	private Long mediaId;
	
	/** 媒资类型 */
	private FolderType mediaType;
	
	/** 流程id */
	private String processInstanceId;
	
	/** 转码输出地址 */
	private String previewUrl;
	
	/** 转码文件本地路径 */
	private String uploadTempPath;
	
	/** 媒资文件名 */
	private String fileName;
	
	/** 媒资文件大小 */
	private Long size;
	
	/** 媒资文件时长 */
	private String duration;

	@Column(name = "MEDIA_ID")
	public Long getMediaId() {
		return mediaId;
	}

	public void setMediaId(Long mediaId) {
		this.mediaId = mediaId;
	}

	@Column(name = "MEDIA_TYPE")
	public FolderType getMediaType() {
		return mediaType;
	}

	public void setMediaType(FolderType mediaType) {
		this.mediaType = mediaType;
	}

	@Column(name = "PROCESS_INSTANCE_ID")
	public String getProcessInstanceId() {
		return processInstanceId;
	}

	public void setProcessInstanceId(String processInstanceId) {
		this.processInstanceId = processInstanceId;
	}

	@Column(name = "PREVIEW_URL")
	public String getPreviewUrl() {
		return previewUrl;
	}

	public void setPreviewUrl(String previewUrl) {
		this.previewUrl = previewUrl;
	}

	@Column(name = "UPLOAD_TEMP_PATH")
	public String getUploadTempPath() {
		return uploadTempPath;
	}

	public void setUploadTempPath(String uploadTempPath) {
		this.uploadTempPath = uploadTempPath;
	}

	@Column(name = "FILE_NAME")
	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	@Column(name = "SIZE")
	public Long getSize() {
		return size;
	}

	public void setSize(Long size) {
		this.size = size;
	}

	@Column(name = "DURATION")
	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}
}
