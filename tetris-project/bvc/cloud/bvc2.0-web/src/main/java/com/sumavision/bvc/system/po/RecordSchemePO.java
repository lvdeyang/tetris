package com.sumavision.bvc.system.po;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.sumavision.tetris.orm.po.AbstractBasePO;

/**
 * @ClassName: 录制方案 <br/>
 * @Description: 录制的是一个角色的屏幕 <br/>
 * @author lvdeyang
 * @date 2018年7月24日 下午3:45:52 
 */
@Entity
@Table(name="BVC_SYSTEM_RECORD_SCHEME")
public class RecordSchemePO extends AbstractBasePO{

	private static final long serialVersionUID = 1L;
	
	/** 方案名称 */
	private String name;
	
	private BusinessRolePO role;
	
	@Column(name = "NAME")
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	@ManyToOne
	@JoinColumn(name = "BUSINESS_ROLE_ID")
	public BusinessRolePO getRole() {
		return role;
	}

	public void setRole(BusinessRolePO role) {
		this.role = role;
	}
	
}
