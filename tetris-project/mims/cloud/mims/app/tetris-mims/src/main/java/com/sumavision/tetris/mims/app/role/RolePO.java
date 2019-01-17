package com.sumavision.tetris.mims.app.role;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import com.sumavision.tetris.mims.app.user.UserClassify;
import com.sumavision.tetris.orm.po.AbstractBasePO;

/**
 * 角色<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2018年11月21日 上午11:59:27
 */
@Entity
@Table(name = "MIMS_ROLE")
public class RolePO extends AbstractBasePO{

	private static final long serialVersionUID = 1L;

	/** 角色名称 */
	private String name;
	
	/** 隶属组织 */
	private String groupId;
	
	/** 是否能删除 */
	private Boolean removeable;
	
	/** 用户类型 */
	private UserClassify classify;
	
	/** 显示排序 */
	private int serial;

	@Column(name = "NAME")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "GROUP_ID")
	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	@Column(name = "REMOVEABLE")
	public Boolean getRemoveable() {
		return removeable;
	}

	public void setRemoveable(Boolean removeable) {
		this.removeable = removeable;
	}
	
	@Enumerated(value = EnumType.STRING)
	@Column(name = "CLASSIFY")
	public UserClassify getClassify() {
		return classify;
	}

	public void setClassify(UserClassify classify) {
		this.classify = classify;
	}

	@Column(name = "SERIAL")
	public int getSerial() {
		return serial;
	}

	public void setSerial(int serial) {
		this.serial = serial;
	}
	
}
