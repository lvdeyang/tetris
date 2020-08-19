package com.sumavision.tetris.mims.app.material;

import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.mvc.converter.AbstractBaseVO;

public class MaterialFileTaskVO extends AbstractBaseVO<MaterialFileTaskVO, MaterialFilePO>{

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

	public MaterialFileTaskVO setLastModified(Long lastModified) {
		this.lastModified = lastModified;
		return this;
	}

	public String getName() {
		return name;
	}

	public MaterialFileTaskVO setName(String name) {
		this.name = name;
		return this;
	}

	public Long getSize() {
		return size;
	}

	public MaterialFileTaskVO setSize(Long size) {
		this.size = size;
		return this;
	}

	public String getMimetype() {
		return mimetype;
	}

	public MaterialFileTaskVO setMimetype(String mimetype) {
		this.mimetype = mimetype;
		return this;
	}

	public String getIcon() {
		return icon;
	}

	public MaterialFileTaskVO setIcon(String icon) {
		this.icon = icon;
		return this;
	}

	public String getStyle() {
		return style;
	}

	public MaterialFileTaskVO setStyle(String style) {
		this.style = style;
		return this;
	}

	public String getType() {
		return type;
	}

	public MaterialFileTaskVO setType(String type) {
		this.type = type;
		return this;
	}

	public Long getFolderId() {
		return folderId;
	}

	public MaterialFileTaskVO setFolderId(Long folderId) {
		this.folderId = folderId;
		return this;
	}

	public String getFolderName() {
		return folderName;
	}

	public MaterialFileTaskVO setFolderName(String folderName) {
		this.folderName = folderName;
		return this;
	}

	public String getFolderNamePath() {
		return folderNamePath;
	}

	public MaterialFileTaskVO setFolderNamePath(String folderNamePath) {
		this.folderNamePath = folderNamePath;
		return this;
	}

	public String getUploadStatus() {
		return uploadStatus;
	}

	public MaterialFileTaskVO setUploadStatus(String uploadStatus) {
		this.uploadStatus = uploadStatus;
		return this;
	}

	public String getPreviewUrl() {
		return previewUrl;
	}

	public MaterialFileTaskVO setPreviewUrl(String previewUrl) {
		this.previewUrl = previewUrl;
		return this;
	}
	
	public Integer getProgress() {
		return progress;
	}

	public MaterialFileTaskVO setProgress(Integer progress) {
		this.progress = progress;
		return this;
	}

	@Override
	public MaterialFileTaskVO set(MaterialFilePO entity) throws Exception {
		this.setId(entity.getId())
			.setUuid(entity.getUuid())
			.setUpdateTime(entity.getUpdateTime()==null?"":DateUtil.format(entity.getUpdateTime(), DateUtil.dateTimePattern))
			.setLastModified(entity.getLastModified())
			.setName(entity.getName())
			.setSize(entity.getSize())
			.setMimetype(entity.getMimetype())
			.setType(entity.getType().toString())
			.setIcon(entity.getType().getIcon())
			//.setStyle(entity.getType().getStyle()[1])
			.setStyle("")
			.setFolderId(entity.getFolderId())
			.setUploadStatus(entity.getUploadStatus().toString())
			.setProgress(0);
		return this;
	}
	
}
