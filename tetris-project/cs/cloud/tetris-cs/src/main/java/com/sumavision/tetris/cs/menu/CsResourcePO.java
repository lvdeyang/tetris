package com.sumavision.tetris.cs.menu;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.sumavision.tetris.orm.po.AbstractBasePO;

@Entity
@Table(name = "TETRIS_CS_MENU_RESOURCE")
public class CsResourcePO extends AbstractBasePO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** 在mims的uuid */
	private String mimsUuid;
	
	/** 在cs父目录id */
	private Long parentId;
	
	/** 在cs父目录路径 */
	private String parentPath;
	
	/** 频道id */
	private Long channelId;
	
	/** 名称 */
	private String name;
	
	/** 预览路径 */
	private String previewUrl;
	
	/** 时长 */
	private String duration;
	
	/** 媒资下载量 */
	private Integer downloadCount;
	
	/** 媒资是否加密 */
	private String encryption;
	
	/** 媒资加密预览地址 */
	private String encryptionUrl;

	@Column(name = "NAME")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "DURATION")
	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}

	@Column(name = "PARENT_ID")
	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}
	
	@Column(name = "PARENT_PATH")
	public String getParentPath() {
		return parentPath;
	}

	public void setParentPath(String parentPath) {
		this.parentPath = parentPath;
	}

	@Column(name = "CHANNEL_ID")
	public Long getChannelId() {
		return channelId;
	}

	public void setChannelId(Long channelId) {
		this.channelId = channelId;
	}

	@Column(name = "MIMS_UUID")
	public String getMimsUuid() {
		return mimsUuid;
	}

	public void setMimsUuid(String mimsUuid) {
		this.mimsUuid = mimsUuid;
	}

	@Column(name = "PREVIEW_URL")
	public String getPreviewUrl() {
		return previewUrl;
	}

	public void setPreviewUrl(String previewUrl) {
		this.previewUrl = previewUrl;
	}

	@Column(name = "DOWNLOAD_COUNT")
	public Integer getDownloadCount() {
		return downloadCount;
	}

	public void setDownloadCount(Integer downloadCount) {
		this.downloadCount = downloadCount;
	}

	@Column(name = "IF_ENCRYPTION")
	public String getEncryption() {
		return encryption;
	}

	public void setEncryption(String encryption) {
		this.encryption = encryption;
	}

	@Column(name = "ENCRYPTION_URL")
	public String getEncryptionUrl() {
		return encryptionUrl;
	}

	public void setEncryptionUrl(String encryptionUrl) {
		this.encryptionUrl = encryptionUrl;
	}
}
