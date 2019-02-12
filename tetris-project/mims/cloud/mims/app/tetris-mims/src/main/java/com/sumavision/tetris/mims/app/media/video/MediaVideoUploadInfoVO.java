package com.sumavision.tetris.mims.app.media.video;

import java.io.File;
import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.mvc.converter.AbstractBaseVO;

public class MediaVideoUploadInfoVO extends AbstractBaseVO<MediaVideoUploadInfoVO, MediaVideoPO>{

	private String name;
	
	private Long lastModified;
	
	private Long size;
	
	private String mimetype;
	
	private Long offset;
	
	public String getName() {
		return name;
	}

	public MediaVideoUploadInfoVO setName(String name) {
		this.name = name;
		return this;
	}

	public Long getLastModified() {
		return lastModified;
	}

	public MediaVideoUploadInfoVO setLastModified(Long lastModified) {
		this.lastModified = lastModified;
		return this;
	}

	public Long getSize() {
		return size;
	}

	public MediaVideoUploadInfoVO setSize(Long size) {
		this.size = size;
		return this;
	}

	public String getMimetype() {
		return mimetype;
	}

	public MediaVideoUploadInfoVO setMimetype(String mimetype) {
		this.mimetype = mimetype;
		return this;
	}

	public Long getOffset() {
		return offset;
	}

	public MediaVideoUploadInfoVO setOffset(Long offset) {
		this.offset = offset;
		return this;
	}

	@Override
	public MediaVideoUploadInfoVO set(MediaVideoPO entity) throws Exception {
		this.setId(entity.getId())
			.setUuid(entity.getUuid())
			.setUpdateTime(entity.getUpdateTime()==null?"":DateUtil.format(entity.getUpdateTime(), DateUtil.dateTimePattern))
			.setName(entity.getName())
			.setSize(entity.getSize())
			.setLastModified(entity.getLastModified())
			.setMimetype(entity.getMimetype())
			.setOffset(new File(entity.getUploadTmpPath()).length());
		return this;
	}
	
}
