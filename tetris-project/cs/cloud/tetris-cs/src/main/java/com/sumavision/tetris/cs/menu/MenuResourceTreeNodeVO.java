package com.sumavision.tetris.cs.menu;

import java.util.ArrayList;
import java.util.List;

public class MenuResourceTreeNodeVO{
	private Long id;
	private String uuid;
	private String updateTime;
	private String name;
	private String type;
	private String mimetype;
	private String duration;
	private Long parentId;
	private String parentPath;
	private Long channelId;
	private String mimsUuid;
	private String previewUrl;
	private Integer downloadCount;
	private String encryption;
	private String encryptionUrl;
	private Boolean checked = true;
	private String remark;
	private List<MenuResourceTreeNodeVO> subColumns;

	public MenuResourceTreeNodeVO set(CsMenuVO entity) {
		MenuResourceTreeNodeVO node = new MenuResourceTreeNodeVO()
				.setId(entity.getId())
				.setUuid(entity.getUuid())
				.setUpdateTime(entity.getUpdateTime())
				.setName(entity.getName())
				.setType("MENU")
				.setParentId(entity.getParentId())
				.setParentPath(entity.getParentPath())
				.setChannelId(entity.getChannelId())
				.setRemark(entity.getRemark());
		return node;
	}
	
	public static List<MenuResourceTreeNodeVO> setAllMenu(List<CsMenuVO> entities) {
		List<MenuResourceTreeNodeVO> returns = new ArrayList<MenuResourceTreeNodeVO>();
		if (entities != null && !entities.isEmpty()) {
			for (CsMenuVO entity : entities) {
				returns.add(new MenuResourceTreeNodeVO().set(entity));
			}
		}
		return returns;
	}
	
	public MenuResourceTreeNodeVO set(CsResourceVO entity) {
		MenuResourceTreeNodeVO node = new MenuResourceTreeNodeVO()
				.setId(entity.getId())
				.setUuid(entity.getUuid())
				.setUpdateTime(entity.getUpdateTime())
				.setName(entity.getName())
				.setType(entity.getType())
				.setMimetype(entity.getMimetype())
				.setDuration(entity.getDuration())
				.setParentId(entity.getParentId())
				.setParentPath(entity.getParentPath())
				.setChannelId(entity.getChannelId())
				.setMimsUuid(entity.getMimsUuid())
				.setPreviewUrl(entity.getPreviewUrl())
				.setDownloadCount(entity.getDownloadCount())
				.setEncryption(entity.getEncryption())
				.setEncryptionUrl(entity.getEncryptionUrl());
		return node;
	}
	
	public static List<MenuResourceTreeNodeVO> setAllResource(List<CsResourceVO> entities) {
		List<MenuResourceTreeNodeVO> returns = new ArrayList<MenuResourceTreeNodeVO>();
		if (entities != null && !entities.isEmpty()) {
			for (CsResourceVO entity : entities) {
				returns.add(new MenuResourceTreeNodeVO().set(entity));
			}
		}
		return returns;
	}

	public Long getId() {
		return id;
	}

	public MenuResourceTreeNodeVO setId(Long id) {
		this.id = id;
		return this;
	}

	public String getUuid() {
		return uuid;
	}

	public MenuResourceTreeNodeVO setUuid(String uuid) {
		this.uuid = uuid;
		return this;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public MenuResourceTreeNodeVO setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
		return this;
	}

	public String getName() {
		return name;
	}

	public MenuResourceTreeNodeVO setName(String name) {
		this.name = name;
		return this;
	}

	public String getType() {
		return type;
	}

	public MenuResourceTreeNodeVO setType(String type) {
		this.type = type;
		return this;
	}

	public String getMimetype() {
		return mimetype;
	}

	public MenuResourceTreeNodeVO setMimetype(String mimetype) {
		this.mimetype = mimetype;
		return this;
	}

	public String getDuration() {
		return duration;
	}

	public MenuResourceTreeNodeVO setDuration(String duration) {
		this.duration = duration;
		return this;
	}

	public Long getParentId() {
		return parentId;
	}

	public MenuResourceTreeNodeVO setParentId(Long parentId) {
		this.parentId = parentId;
		return this;
	}

	public String getParentPath() {
		return parentPath;
	}

	public MenuResourceTreeNodeVO setParentPath(String parentPath) {
		this.parentPath = parentPath;
		return this;
	}

	public Long getChannelId() {
		return channelId;
	}

	public MenuResourceTreeNodeVO setChannelId(Long channelId) {
		this.channelId = channelId;
		return this;
	}

	public String getMimsUuid() {
		return mimsUuid;
	}

	public MenuResourceTreeNodeVO setMimsUuid(String mimsUuid) {
		this.mimsUuid = mimsUuid;
		return this;
	}

	public String getPreviewUrl() {
		return previewUrl;
	}

	public MenuResourceTreeNodeVO setPreviewUrl(String previewUrl) {
		this.previewUrl = previewUrl;
		return this;
	}

	public Integer getDownloadCount() {
		return downloadCount;
	}

	public MenuResourceTreeNodeVO setDownloadCount(Integer downloadCount) {
		this.downloadCount = downloadCount;
		return this;
	}

	public String getEncryption() {
		return encryption;
	}

	public MenuResourceTreeNodeVO setEncryption(String encryption) {
		this.encryption = encryption;
		return this;
	}

	public String getEncryptionUrl() {
		return encryptionUrl;
	}

	public MenuResourceTreeNodeVO setEncryptionUrl(String encryptionUrl) {
		this.encryptionUrl = encryptionUrl;
		return this;
	}

	public Boolean getChecked() {
		return checked;
	}

	public MenuResourceTreeNodeVO setChecked(Boolean checked) {
		this.checked = checked;
		return this;
	}

	public String getRemark() {
		return remark;
	}

	public MenuResourceTreeNodeVO setRemark(String remark) {
		this.remark = remark;
		return this;
	}

	public List<MenuResourceTreeNodeVO> getSubColumns() {
		return subColumns;
	}

	public MenuResourceTreeNodeVO setSubColumns(List<MenuResourceTreeNodeVO> subColumns) {
		this.subColumns = subColumns;
		return this;
	}
}
