package com.sumavision.tetris.mims.app.media.compress;

import java.io.File;
import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.mvc.converter.AbstractBaseVO;

public class MediaCompressUploadInfoVO extends AbstractBaseVO<MediaCompressUploadInfoVO, MediaCompressPO>{

	private String name;
	
	private Long lastModified;
	
	private Long size;
	
	private String mimetype;
	
	private Long offset;
	
	public String getName() {
		return name;
	}

	public MediaCompressUploadInfoVO setName(String name) {
		this.name = name;
		return this;
	}

	public Long getLastModified() {
		return lastModified;
	}

	public MediaCompressUploadInfoVO setLastModified(Long lastModified) {
		this.lastModified = lastModified;
		return this;
	}

	public Long getSize() {
		return size;
	}

	public MediaCompressUploadInfoVO setSize(Long size) {
		this.size = size;
		return this;
	}

	public String getMimetype() {
		return mimetype;
	}

	public MediaCompressUploadInfoVO setMimetype(String mimetype) {
		this.mimetype = mimetype;
		return this;
	}

	public Long getOffset() {
		return offset;
	}

	public MediaCompressUploadInfoVO setOffset(Long offset) {
		this.offset = offset;
		return this;
	}

	@Override
	public MediaCompressUploadInfoVO set(MediaCompressPO entity) throws Exception {
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
