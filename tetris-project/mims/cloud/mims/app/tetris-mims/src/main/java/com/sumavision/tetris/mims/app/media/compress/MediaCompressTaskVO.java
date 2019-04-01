package com.sumavision.tetris.mims.app.media.compress;

import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.mvc.converter.AbstractBaseVO;

public class MediaCompressTaskVO  extends AbstractBaseVO<MediaCompressTaskVO, MediaCompressPO>{

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
	
	private Integer progress;
	
	public Long getLastModified() {
		return lastModified;
	}

	public MediaCompressTaskVO setLastModified(Long lastModified) {
		this.lastModified = lastModified;
		return this;
	}

	public String getName() {
		return name;
	}

	public MediaCompressTaskVO setName(String name) {
		this.name = name;
		return this;
	}

	public Long getSize() {
		return size;
	}

	public MediaCompressTaskVO setSize(Long size) {
		this.size = size;
		return this;
	}

	public String getMimetype() {
		return mimetype;
	}

	public MediaCompressTaskVO setMimetype(String mimetype) {
		this.mimetype = mimetype;
		return this;
	}

	public String getIcon() {
		return icon;
	}

	public MediaCompressTaskVO setIcon(String icon) {
		this.icon = icon;
		return this;
	}

	public String getStyle() {
		return style;
	}

	public MediaCompressTaskVO setStyle(String style) {
		this.style = style;
		return this;
	}

	public String getType() {
		return type;
	}

	public MediaCompressTaskVO setType(String type) {
		this.type = type;
		return this;
	}

	public Long getFolderId() {
		return folderId;
	}

	public MediaCompressTaskVO setFolderId(Long folderId) {
		this.folderId = folderId;
		return this;
	}

	public String getFolderName() {
		return folderName;
	}

	public MediaCompressTaskVO setFolderName(String folderName) {
		this.folderName = folderName;
		return this;
	}

	public String getFolderNamePath() {
		return folderNamePath;
	}

	public MediaCompressTaskVO setFolderNamePath(String folderNamePath) {
		this.folderNamePath = folderNamePath;
		return this;
	}

	public String getUploadStatus() {
		return uploadStatus;
	}

	public MediaCompressTaskVO setUploadStatus(String uploadStatus) {
		this.uploadStatus = uploadStatus;
		return this;
	}
	
	public Integer getProgress() {
		return progress;
	}

	public MediaCompressTaskVO setProgress(Integer progress) {
		this.progress = progress;
		return this;
	}
	
	@Override
	public MediaCompressTaskVO set(MediaCompressPO entity) throws Exception {
		this.setId(entity.getId())
			.setUuid(entity.getUuid())
			.setUpdateTime(entity.getUpdateTime()==null?"":DateUtil.format(entity.getUpdateTime(), DateUtil.dateTimePattern))
			.setLastModified(entity.getLastModified())
			.setName(entity.getName())
			.setSize(entity.getSize())
			.setMimetype(entity.getMimetype())
			.setType(MediaCompressItemType.COMPRESS.toString())
			.setIcon(MediaCompressItemType.COMPRESS.getIcon())
			.setStyle(MediaCompressItemType.COMPRESS.getStyle()[0])
			.setFolderId(entity.getFolderId())
			.setUploadStatus(entity.getUploadStatus().toString())
			.setProgress(0);
		return this;
	}

}
