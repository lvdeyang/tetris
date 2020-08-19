package com.sumavision.tetris.mims.app.media.audio;

import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.mvc.converter.AbstractBaseVO;

public class MediaAudioTaskVO  extends AbstractBaseVO<MediaAudioTaskVO, MediaAudioPO>{

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
	
	public Long getLastModified() {
		return lastModified;
	}

	public MediaAudioTaskVO setLastModified(Long lastModified) {
		this.lastModified = lastModified;
		return this;
	}

	public String getName() {
		return name;
	}

	public MediaAudioTaskVO setName(String name) {
		this.name = name;
		return this;
	}

	public Long getSize() {
		return size;
	}

	public MediaAudioTaskVO setSize(Long size) {
		this.size = size;
		return this;
	}

	public String getMimetype() {
		return mimetype;
	}

	public MediaAudioTaskVO setMimetype(String mimetype) {
		this.mimetype = mimetype;
		return this;
	}

	public String getIcon() {
		return icon;
	}

	public MediaAudioTaskVO setIcon(String icon) {
		this.icon = icon;
		return this;
	}

	public String getStyle() {
		return style;
	}

	public MediaAudioTaskVO setStyle(String style) {
		this.style = style;
		return this;
	}

	public String getType() {
		return type;
	}

	public MediaAudioTaskVO setType(String type) {
		this.type = type;
		return this;
	}

	public Long getFolderId() {
		return folderId;
	}

	public MediaAudioTaskVO setFolderId(Long folderId) {
		this.folderId = folderId;
		return this;
	}

	public String getFolderName() {
		return folderName;
	}

	public MediaAudioTaskVO setFolderName(String folderName) {
		this.folderName = folderName;
		return this;
	}

	public String getFolderNamePath() {
		return folderNamePath;
	}

	public MediaAudioTaskVO setFolderNamePath(String folderNamePath) {
		this.folderNamePath = folderNamePath;
		return this;
	}

	public String getUploadStatus() {
		return uploadStatus;
	}

	public MediaAudioTaskVO setUploadStatus(String uploadStatus) {
		this.uploadStatus = uploadStatus;
		return this;
	}

	public String getPreviewUrl() {
		return previewUrl;
	}

	public MediaAudioTaskVO setPreviewUrl(String previewUrl) {
		this.previewUrl = previewUrl;
		return this;
	}
	
	public Integer getProgress() {
		return progress;
	}

	public MediaAudioTaskVO setProgress(Integer progress) {
		this.progress = progress;
		return this;
	}
	
	@Override
	public MediaAudioTaskVO set(MediaAudioPO entity) throws Exception {
		this.setId(entity.getId())
			.setUuid(entity.getUuid())
			.setUpdateTime(entity.getUpdateTime()==null?"":DateUtil.format(entity.getUpdateTime(), DateUtil.dateTimePattern))
			.setLastModified(entity.getLastModified())
			.setName(entity.getName())
			.setSize(entity.getSize())
			.setMimetype(entity.getMimetype())
			.setType(MediaAudioItemType.AUDIO.toString())
			.setIcon(MediaAudioItemType.AUDIO.getIcon())
			//.setStyle(entity.getType().getStyle()[1])
			.setStyle(MediaAudioItemType.AUDIO.getStyle()[0])
			.setFolderId(entity.getFolderId())
			.setUploadStatus(entity.getUploadStatus().toString())
			.setProgress(0);
		return this;
	}
	
}
