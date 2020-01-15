package com.sumavision.bvc.system.po;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import com.sumavision.bvc.system.enumeration.DicType;
import com.sumavision.bvc.system.enumeration.ServLevel;
import com.sumavision.tetris.orm.po.AbstractBasePO;

@Entity
@Table(name="BVC_SYSTEM_DICTIONARY")
public class DictionaryPO extends AbstractBasePO{

	private static final long serialVersionUID = 1L;
	
	/*
	 * 名字，name
	 */
	private String content;

	private DicType dicType;
	
	/*
	 * 点播栏目、地区的boId
	 */
	private String boId;
	
	/*
	 * 地区的区域码
	 */
	private String code;
	
	/*
	 * 点播二级栏目所述的一级栏目的boId
	 */
	private String parentBoId;
	
	private String ip;
	
	/*
	 * 直播栏目的boId
	 */
	private String liveBoId;
	
	/*
	 * 所属地区ID
	 */
	private String parentRegionId;
	
	private ServLevel servLevel; 
	
	private Long parentLevelId;
	
	private String parentLevelName; 
	
	public String getLiveBoId() {
		return liveBoId;
	}

	public void setLiveBoId(String liveBoId) {
		this.liveBoId = liveBoId;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getParentBoId() {
		return parentBoId;
	}

	public void setParentBoId(String parentBoId) {
		this.parentBoId = parentBoId;
	}

	@Column(name = "CONTENT")
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	@Enumerated(value = EnumType.STRING)
	@Column(name = "DIC_TYPE")
	public DicType getDicType() {
		return dicType;
	}

	public void setDicType(DicType type) {
		this.dicType = type;
	}
	@Column(name = "BO_ID")
	public String getBoId() {
		return boId;
	}

	public void setBoId(String boId) {
		this.boId = boId;
	}

	@Column(length = 2048)
	public String getParentRegionId() {
		return parentRegionId;
	}

	public void setParentRegionId(String parentRegionId) {
		this.parentRegionId = parentRegionId;
	}

	@Enumerated(value = EnumType.STRING)
	@Column(name = "SERV_LEVEL")
	public ServLevel getServLevel() {
		return servLevel;
	}

	public void setServLevel(ServLevel servLevel) {
		this.servLevel = servLevel;
	}

	@Column(name = "PARENT_LEVEL_ID")
	public Long getParentLevelId() {
		return parentLevelId;
	}

	public void setParentLevelId(Long parentLevelId) {
		this.parentLevelId = parentLevelId;
	}

	@Column(name = "PARENT_LEVEL_NAME")
	public String getParentLevelName() {
		return parentLevelName;
	}

	public void setParentLevelName(String parentLevelName) {
		this.parentLevelName = parentLevelName;
	}

	

}
