package com.sumavision.tetris.cms.region;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.sumavision.tetris.orm.po.AbstractBasePO;

/**
 * 地区数据<br/>
 * <b>作者: </b>ldy<br/>
 * <b>版本: </b>1.0<br/> 
 * <b>日期: </b>2019年2月28日 下午1:27:28
 */
@Entity
@Table(name = "TETRIS_CMS_REGION")
public class RegionPO extends AbstractBasePO{

	/** 这是一个常量的说明 */
	private static final long serialVersionUID = 1L;
	
	/** 地区名 */
	private String name;
	
	/** 地区编号 */
	private String code;
	
	/** 地区服务器ip */
	private String ip;
	
	/** 父地区id */
	private Long parentId;
	
	/** 上级地区id路径：/id/id/id */
	private String parentPath;

	@Column(name = "NAME")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "CODE")
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@Column(name = "IP")
	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	@Column(name = "PARENT_ID")
	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	@Column(name = "PARENT_PATH")
	public String getParentPath() {
		return parentPath;
	}

	public void setParentPath(String parentPath) {
		this.parentPath = parentPath;
	}
	
}
