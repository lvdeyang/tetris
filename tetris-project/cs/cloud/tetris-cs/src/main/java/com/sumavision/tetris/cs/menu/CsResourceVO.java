package com.sumavision.tetris.cs.menu;

import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.mvc.converter.AbstractBaseVO;

public class CsResourceVO extends AbstractBaseVO<CsResourceVO,CsResourcePO>{

	private String name;
	private String type;
	private String mimetype;
	private String duration;
	private Long parentId;
	private String parentPath;
	private Long channelId;
	private String mimsUuid;
	private Long index;
	private String previewUrl;
	private Integer downloadCount;
	private String encryption;
	private String encryptionUrl;
	private String freq;
	private String audioPid;
	private String videoPid;
	private Boolean checked = true;
	
	@Override
	public CsResourceVO set(CsResourcePO entity) throws Exception {
		this.setId(entity.getId())
		.setUpdateTime(entity.getUpdateTime() == null ? "": DateUtil.format(entity.getUpdateTime(), DateUtil.dateTimePattern))
		.setUuid(entity.getUuid())
		.setName(entity.getName())
		.setType(entity.getType())
		.setMimetype(entity.getMimetype())
		.setDuration(entity.getDuration())
		.setMimsUuid(entity.getMimsUuid())
		.setParentId(entity.getParentId())
		.setParentPath(entity.getParentPath())
		.setChannelId(entity.getChannelId())
		.setEncryptionUrl(entity.getEncryptionUrl())
		.setDownloadCount(entity.getDownloadCount())
		.setPreviewUrl(entity.getPreviewUrl())
		.setFreq(entity.getFreq())
		.setAudioPid(entity.getAudioPid())
		.setVideoPid(entity.getVideoPid());
		
		return this;
	}

	public String getName() {
		return name;
	}

	public CsResourceVO setName(String name) {
		this.name = name;
		return this;
	}

	public String getType() {
		return type;
	}

	public CsResourceVO setType(String type) {
		this.type = type;
		return this;
	}

	public String getMimetype() {
		return mimetype;
	}

	public CsResourceVO setMimetype(String mimetype) {
		this.mimetype = mimetype;
		return this;
	}

	public String getDuration() {
		return duration;
	}

	public CsResourceVO setDuration(String duration) {
		this.duration = duration;
		return this;
	}

	public Long getParentId() {
		return parentId;
	}

	public CsResourceVO setParentId(Long parentId) {
		this.parentId = parentId;
		return this;
	}
	
	public String getParentPath() {
		return parentPath;
	}

	public CsResourceVO setParentPath(String parentPath) {
		this.parentPath = parentPath;
		return this;
	}

	public Long getChannelId() {
		return channelId;
	}

	public CsResourceVO setChannelId(Long channelId) {
		this.channelId = channelId;
		return this;
	}

	public String getMimsUuid() {
		return mimsUuid;
	}

	public CsResourceVO setMimsUuid(String mimsUuid) {
		this.mimsUuid = mimsUuid;
		return this;
	}
	
	public String getPreviewUrl() {
		return previewUrl;
	}

	public CsResourceVO setPreviewUrl(String previewUrl) {
		this.previewUrl = previewUrl;
		return this;
	}

	public Integer getDownloadCount() {
		return downloadCount;
	}

	public CsResourceVO setDownloadCount(Integer downloadCount) {
		this.downloadCount = downloadCount;
		return this;
	}

	public String getEncryption() {
		return encryption;
	}

	public CsResourceVO setEncryption(String encryption) {
		this.encryption = encryption;
		return this;
	}

	public String getEncryptionUrl() {
		return encryptionUrl;
	}

	public CsResourceVO setEncryptionUrl(String encryptionUrl) {
		this.encryptionUrl = encryptionUrl;
		return this;
	}

	public String getFreq() {
		return freq;
	}

	public CsResourceVO setFreq(String freq) {
		this.freq = freq;
		return this;
	}

	public String getAudioPid() {
		return audioPid;
	}

	public CsResourceVO setAudioPid(String audioPid) {
		this.audioPid = audioPid;
		return this;
	}

	public String getVideoPid() {
		return videoPid;
	}

	public CsResourceVO setVideoPid(String videoPid) {
		this.videoPid = videoPid;
		return this;
	}

	public Boolean getChecked() {
		return checked;
	}

	public void setChecked(Boolean checked) {
		this.checked = checked;
	}

	public Long getIndex() {
		return index;
	}

	public void setIndex(Long index) {
		this.index = index;
	}
}
