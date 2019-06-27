package com.sumavision.tetris.subordinate.role;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import com.sumavision.tetris.orm.po.AbstractBasePO;

/**
 * 隶属角色<br/>
 * <b>作者:</b>lzp<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2019年5月29日 上午9:12:29
 */
@Entity
@Table(name = "TETRIS_SUBORDINATE_ROLE")
public class SubordinateRolePO extends AbstractBasePO{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/* 角色名称 */
	private String name;
	
	/* 角色所属公司id */
	private Long companyId;
	
	/* 角色所属公司管理员id */
	private String userId;
	
	
	/** 是否能删除 */
	private Boolean removeable;
	
	/** 用户类型 */
	private SubordinateRoleClassify classify;
	
	/** 显示排序 */
	private int serial;


	@Column(name = "NAME")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "COMPANY_ID")
	public Long getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}

	@Column(name = "USER_ID")
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
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
	public SubordinateRoleClassify getClassify() {
		return classify;
	}

	public void setClassify(SubordinateRoleClassify classify) {
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
