package com.sumavision.tetris.mims.app.media.picture;

import java.io.File;
import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.mvc.converter.AbstractBaseVO;

public class MediaPictureUploadInfoVO extends AbstractBaseVO<MediaPictureUploadInfoVO, MediaPicturePO>{

	private String name;
	
	private Long lastModified;
	
	private Long size;
	
	private String mimetype;
	
	private Long offset;
	
	public String getName() {
		return name;
	}

	public MediaPictureUploadInfoVO setName(String name) {
		this.name = name;
		return this;
	}

	public Long getLastModified() {
		return lastModified;
	}

	public MediaPictureUploadInfoVO setLastModified(Long lastModified) {
		this.lastModified = lastModified;
		return this;
	}

	public Long getSize() {
		return size;
	}

	public MediaPictureUploadInfoVO setSize(Long size) {
		this.size = size;
		return this;
	}

	public String getMimetype() {
		return mimetype;
	}

	public MediaPictureUploadInfoVO setMimetype(String mimetype) {
		this.mimetype = mimetype;
		return this;
	}

	public Long getOffset() {
		return offset;
	}

	public MediaPictureUploadInfoVO setOffset(Long offset) {
		this.offset = offset;
		return this;
	}

	@Override
	public MediaPictureUploadInfoVO set(MediaPicturePO entity) throws Exception {
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
