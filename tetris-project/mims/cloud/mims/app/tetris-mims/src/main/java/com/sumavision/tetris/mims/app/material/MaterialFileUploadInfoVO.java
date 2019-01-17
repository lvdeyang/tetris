package com.sumavision.tetris.mims.app.material;

import java.io.File;
import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.mvc.converter.AbstractBaseVO;

/**
 * 描述文件上传情况<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2018年12月3日 下午5:04:32
 */
public class MaterialFileUploadInfoVO extends AbstractBaseVO<MaterialFileUploadInfoVO, MaterialFilePO>{

	private String name;
	
	private Long lastModified;
	
	private Long size;
	
	private String mimetype;
	
	private Long offset;
	
	public String getName() {
		return name;
	}

	public MaterialFileUploadInfoVO setName(String name) {
		this.name = name;
		return this;
	}

	public Long getLastModified() {
		return lastModified;
	}

	public MaterialFileUploadInfoVO setLastModified(Long lastModified) {
		this.lastModified = lastModified;
		return this;
	}

	public Long getSize() {
		return size;
	}

	public MaterialFileUploadInfoVO setSize(Long size) {
		this.size = size;
		return this;
	}

	public String getMimetype() {
		return mimetype;
	}

	public MaterialFileUploadInfoVO setMimetype(String mimetype) {
		this.mimetype = mimetype;
		return this;
	}

	public Long getOffset() {
		return offset;
	}

	public MaterialFileUploadInfoVO setOffset(Long offset) {
		this.offset = offset;
		return this;
	}

	@Override
	public MaterialFileUploadInfoVO set(MaterialFilePO entity) throws Exception {
		this.setId(entity.getId())
			.setUuid(entity.getUuid())
			.setUpdateTime(entity.getUpdateTime()==null?"":DateUtil.format(entity.getUpdateTime(), DateUtil.dateTimePattern))
			.setName(entity.getName())
			.setSize(entity.getSize())
			.setLastModified(entity.getLastModified())
			.setMimetype(entity.getMimetype())
			.setOffset(new File(entity.getTmpPath()).length());
		return this;
	}
	
}
