package com.sumavision.tetris.cs.menu;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.sumavision.tetris.orm.po.AbstractBasePO;

@Entity
@Table(name = "TETRIS_CS_CHANNEL_MENU")
public class CsMenuPO extends AbstractBasePO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** 频道id */
	private Long ChannelId;
	
	/** 目录名称 */
	private String name;
	
	/** 父目录id */
	private Long parentId;
	
	/** 面包片路径 */
	private String parentPath;
	
	/** 备注 */
	private String remark;

	@Column(name = "CHANNEL_ID")
	public Long getChannelId() {
		return ChannelId;
	}

	public void setChannelId(Long channelId) {
		ChannelId = channelId;
	}

	@Column(name = "NAME")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	@Column(name = "REMARK")
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	
}
