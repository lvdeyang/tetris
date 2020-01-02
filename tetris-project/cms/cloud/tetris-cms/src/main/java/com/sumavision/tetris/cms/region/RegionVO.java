package com.sumavision.tetris.cms.region;

import java.util.List;

import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.mvc.converter.AbstractBaseVO;

public class RegionVO extends AbstractBaseVO<RegionVO, RegionPO>{
	
	private String name;
	
	private String code;
	
	private String ip;
	
	private Long parentId;
	
	private List<RegionVO> subRegions;

	@Override
	public RegionVO set(RegionPO entity) throws Exception {
		this.setId(entity.getId())
			.setUuid(entity.getUuid())
			.setUpdateTime(entity.getUpdateTime()==null?"":DateUtil.format(entity.getUpdateTime(), DateUtil.dateTimePattern))
			.setParentId(entity.getParentId())
			.setName(entity.getName())
			.setCode(entity.getCode())
			.setIp(entity.getIp());		
		return this;
	}

	public String getName() {
		return name;
	}

	public RegionVO setName(String name) {
		this.name = name;
		return this;
	}

	public Long getParentId() {
		return parentId;
	}

	public RegionVO setParentId(Long parentId) {
		this.parentId = parentId;
		return this;
	}

	public String getCode() {
		return code;
	}

	public RegionVO setCode(String code) {
		this.code = code;
		return this;
	}

	public String getIp() {
		return ip;
	}

	public RegionVO setIp(String ip) {
		this.ip = ip;
		return this;
	}

	public List<RegionVO> getSubRegions() {
		return subRegions;
	}

	public RegionVO setSubRegions(List<RegionVO> subRegions) {
		this.subRegions = subRegions;
		return this;
	}

}
