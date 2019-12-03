package com.sumavision.tetris.cs.program;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.sumavision.tetris.orm.po.AbstractBasePO;

@Entity
@Table(name = "TETRIS_CS_SCREEN")
public class ScreenPO extends AbstractBasePO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** 分屏id */
	private Long programId;
	
	/** 分屏位置 */
	private Long serialNum;
	
	/** 排单顺序 */
	private Long screenIndex;
	
	/** 媒资在cs中的id */
	private Long resourceId;
	
	/** 媒资在mims中的uuid */
	private String mimsUuid;
	
	/** 媒资名称 */
	private String name;
	
	/** 媒资类型 */
	private String type;
	
	/** 媒资的mimetype */
	private String mimetype;
	
	/** 媒资预览地址 */
	private String previewUrl;
	
	/** 媒资加密预览地址 */
	private String encryptionUrl;
	
	/** 媒资热度权重 */
	private Integer hotWeight;
	
	/** 媒资下载量 */
	private Integer downloadCount;
	
	/** 媒资时长 */
	private String duration;
	
	@Column(name="PROGRAM_ID")
	public Long getProgramId() {
		return programId;
	}

	public void setProgramId(Long programId) {
		this.programId = programId;
	}

	@Column(name="SERIAL_NUM")
	public Long getSerialNum() {
		return serialNum;
	}

	public void setSerialNum(Long serialNum) {
		this.serialNum = serialNum;
	}
	
	@Column(name="SCREEN_INDEX")
	public void setScreenIndex(Long screenIndex) {
		this.screenIndex = screenIndex;
	}
	
	public Long getScreenIndex() {
		return screenIndex;
	}
	
	@Column(name="RESOURCE_ID")
	public Long getResourceId() {
		return resourceId;
	}

	public void setResourceId(Long resourceId) {
		this.resourceId = resourceId;
	}

	@Column(name="MIMS_UUID")
	public String getMimsUuid() {
		return mimsUuid;
	}

	public void setMimsUuid(String mimsUuid) {
		this.mimsUuid = mimsUuid;
	}

	@Column(name="NAME")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@Column(name = "TYPE")
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Column(name = "MIMETYPE")
	public String getMimetype() {
		return mimetype;
	}

	public void setMimetype(String mimetype) {
		this.mimetype = mimetype;
	}

	@Column(name="PREVIEW_URL")
	public String getPreviewUrl() {
		return previewUrl;
	}

	public void setPreviewUrl(String previewUrl) {
		this.previewUrl = previewUrl;
	}

	@Column(name = "ENCRYPTION_URL")
	public String getEncryptionUrl() {
		return encryptionUrl;
	}

	public void setEncryptionUrl(String encryptionUrl) {
		this.encryptionUrl = encryptionUrl;
	}

	@Column(name = "HOT_WEIGHT")
	public Integer getHotWeight() {
		return hotWeight;
	}

	public void setHotWeight(Integer hotWeight) {
		this.hotWeight = hotWeight;
	}

	@Column(name = "DOWNLOAD_COUNT")
	public Integer getDownloadCount() {
		return downloadCount;
	}

	public void setDownloadCount(Integer downloadCount) {
		this.downloadCount = downloadCount;
	}

	@Column(name = "DURATION")
	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}
}
