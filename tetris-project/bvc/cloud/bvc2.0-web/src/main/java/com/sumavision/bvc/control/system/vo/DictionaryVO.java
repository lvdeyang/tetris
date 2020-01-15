package com.sumavision.bvc.control.system.vo;

import com.sumavision.bvc.system.po.DictionaryPO;
import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.mvc.converter.AbstractBaseVO;

public class DictionaryVO extends AbstractBaseVO<DictionaryVO, DictionaryPO>{

	private String content;
	
	private String dicType;
	
	private String boId;
	
	private String liveBoId;
	
    private String code;
	
	private String parentBoId;
	
	private String ip;
	
	private String parentRegionId;
	
	private String servLevel;
	
	private Long parentLevelId;
	
	private String parentLevelName;
	
	public String getContent() {
		return content;
	}

	public DictionaryVO setContent(String content) {
		this.content = content;
		return this;
	}

	public String getDicType() {
		return dicType;
	}

	public DictionaryVO setDicType(String type) {
		this.dicType = type;
		return this;
	}

	public String getBoId() {
		return boId;
	}

	public DictionaryVO setBoId(String boId) {
		this.boId = boId;
		return this;
	}

	public String getCode() {
		return code;
	}

	public DictionaryVO setCode(String code) {
		this.code = code;
		return this;
	}

	public String getParentBoId() {
		return parentBoId;
	}

	public DictionaryVO setParentBoId(String parentBoId) {
		this.parentBoId = parentBoId;
		return this;
	}

	public String getIp() {
		return ip;
	}

	public DictionaryVO setIp(String ip) {
		this.ip = ip;
		return this;
	}

	public String getParentRegionId() {
		return parentRegionId;
	}

	public DictionaryVO setParentRegionId(String parentRegionId) {
		this.parentRegionId = parentRegionId;
		return this;
	}
	 
	public String getServLevel() {
		return servLevel;
	}

	public DictionaryVO setServLevel(String servType) {
		this.servLevel = servType;
		return this;
	}

	public Long getParentLevelId() {
		return parentLevelId;
	}

	public DictionaryVO setParentLevelId(Long parentLevelId) {
		this.parentLevelId = parentLevelId;
		return this;
	}

	public String getParentLevelName() {
		return parentLevelName;
	}

	public DictionaryVO setParentLevelName(String parentLevelName) {
		this.parentLevelName = parentLevelName;
		return this;
	}

	@Override
	public DictionaryVO set(DictionaryPO entity) throws Exception {
		// TODO Auto-generated method stub
		this.setId(entity.getId())
		.setUuid(entity.getUuid())
		.setUpdateTime(entity.getUpdateTime()==null?"":DateUtil.format(entity.getUpdateTime(), DateUtil.dateTimePattern))
		.setContent(entity.getContent())
		.setBoId(entity.getBoId())
		.setLiveBoId(entity.getLiveBoId())
		.setCode(entity.getCode())
		.setParentBoId(entity.getParentBoId())
		.setIp(entity.getIp())
		.setParentRegionId(entity.getParentRegionId())
		.setDicType(entity.getDicType()==null?"":entity.getDicType().getName())
		.setServLevel(entity.getServLevel()==null?"":entity.getServLevel().getName())
		.setParentLevelId(entity.getParentLevelId())
		.setParentLevelName(entity.getParentLevelName());
		return this;
	}

	public String getLiveBoId() {
		return liveBoId;
	}

	public DictionaryVO setLiveBoId(String liveBoId) {
		this.liveBoId = liveBoId;
		return this;
	}

	
}
