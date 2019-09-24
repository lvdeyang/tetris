package com.sumavision.tetris.mims.app.media.txt;

import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.mims.app.media.video.MediaVideoItemType;
import com.sumavision.tetris.mvc.converter.AbstractBaseVO;

public class MediaTxtTaskVO extends AbstractBaseVO<MediaTxtTaskVO, MediaTxtPO>{
	private Long lastModified;
	
	private String name;
	
	private Long size;
	
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

	public MediaTxtTaskVO setLastModified(Long lastModified) {
		this.lastModified = lastModified;
		return this;
	}

	public String getName() {
		return name;
	}

	public MediaTxtTaskVO setName(String name) {
		this.name = name;
		return this;
	}

	public Long getSize() {
		return size;
	}

	public MediaTxtTaskVO setSize(Long size) {
		this.size = size;
		return this;
	}

	public String getType() {
		return type;
	}

	public MediaTxtTaskVO setType(String type) {
		this.type = type;
		return this;
	}

	public String getIcon() {
		return icon;
	}

	public MediaTxtTaskVO setIcon(String icon) {
		this.icon = icon;
		return this;
	}

	public String getStyle() {
		return style;
	}

	public MediaTxtTaskVO setStyle(String style) {
		this.style = style;
		return this;
	}

	public Long getFolderId() {
		return folderId;
	}

	public MediaTxtTaskVO setFolderId(Long folderId) {
		this.folderId = folderId;
		return this;
	}

	public String getFolderName() {
		return folderName;
	}

	public MediaTxtTaskVO setFolderName(String folderName) {
		this.folderName = folderName;
		return this;
	}

	public String getFolderNamePath() {
		return folderNamePath;
	}

	public MediaTxtTaskVO setFolderNamePath(String folderNamePath) {
		this.folderNamePath = folderNamePath;
		return this;
	}

	public String getUploadStatus() {
		return uploadStatus;
	}

	public MediaTxtTaskVO setUploadStatus(String uploadStatus) {
		this.uploadStatus = uploadStatus;
		return this;
	}

	public String getPreviewUrl() {
		return previewUrl;
	}

	public MediaTxtTaskVO setPreviewUrl(String previewUrl) {
		this.previewUrl = previewUrl;
		return this;
	}

	public Integer getProgress() {
		return progress;
	}

	public MediaTxtTaskVO setProgress(Integer progress) {
		this.progress = progress;
		return this;
	}

	@Override
	public MediaTxtTaskVO set(MediaTxtPO entity) throws Exception {
		this.setId(entity.getId())
		.setUuid(entity.getUuid())
		.setUpdateTime(entity.getUpdateTime()==null?"":DateUtil.format(entity.getUpdateTime(), DateUtil.dateTimePattern))
//		.setLastModified(entity.getLastModified())
		.setName(entity.getName())
		.setSize(entity.getSize())
//		.setType(MediaVideoItemType.VIDEO.toString())
//		.setIcon(MediaVideoItemType.VIDEO.getIcon())
//		.setStyle(MediaVideoItemType.VIDEO.getStyle()[0])
		.setFolderId(entity.getFolderId())
		.setUploadStatus(entity.getUploadStatus().toString())
		.setProgress(0);
		return this;
	}
}
