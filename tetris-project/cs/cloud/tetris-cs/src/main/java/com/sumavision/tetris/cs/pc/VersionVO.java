package com.sumavision.tetris.cs.pc;

import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.cs.program.ProgramVO;
import com.sumavision.tetris.mvc.converter.AbstractBaseVO;

public class VersionVO extends AbstractBaseVO<VersionVO, VersionPO>{
	
	/** 版本号 */
	private String version;
	
	/** url */
	private String url;
	
	private long size;
	
	private String name;
	
	

	public String getVersion() {
		return version;
	}



	public VersionVO setVersion(String version) {
		this.version = version;
		return this;
	}



	public String getUrl() {
		return url;
	}



	public VersionVO setUrl(String url) {
		this.url = url;
		return this;
	}



	public long getSize() {
		return size;
	}



	public VersionVO setSize(long size) {
		this.size = size;
		return this;
	}



	public String getName() {
		return name;
	}



	public VersionVO setName(String name) {
		this.name = name;
		return this;
	}



	@Override
	public VersionVO set(VersionPO entity) throws Exception {
		this.setId(entity.getId())
		.setUuid(entity.getUuid())
		.setUpdateTime(entity.getUpdateTime() == null ? "": DateUtil.format(entity.getUpdateTime(), DateUtil.dateTimePattern))
		.setVersion(entity.getVersion())
		.setVersion(entity.getVersion())
		.setName(entity.getName())
		.setUrl(entity.getUrl());
		return this;
	}
}
