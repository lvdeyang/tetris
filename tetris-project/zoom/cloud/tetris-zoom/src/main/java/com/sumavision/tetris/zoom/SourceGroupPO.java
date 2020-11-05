package com.sumavision.tetris.zoom;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import com.sumavision.tetris.orm.po.AbstractBasePO;

/**
 * 资源分组<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2020年3月2日 上午10:01:08
 */
@Entity
@Table(name = "TETRIS_SOURCE_GROUP")
public class SourceGroupPO extends AbstractBasePO{

	private static final long serialVersionUID = 1L;
	
	/** 分组名称 */
	private String name;
	
	/** 分组类型 */
	private SourceGroupType type;
	
	/** 用户id */
	private String userId;

	@Column(name = "NAME")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Enumerated(value = EnumType.STRING)
	@Column(name = "TYPE")
	public SourceGroupType getType() {
		return type;
	}

	public void setType(SourceGroupType type) {
		this.type = type;
	}

	@Column(name = "USER_ID")
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
	
}
