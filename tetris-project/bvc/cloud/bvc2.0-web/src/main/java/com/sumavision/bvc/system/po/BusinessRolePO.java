package com.sumavision.bvc.system.po;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.sumavision.bvc.system.enumeration.BusinessRoleSpecial;
import com.sumavision.bvc.system.enumeration.BusinessRoleType;
import com.sumavision.tetris.orm.po.AbstractBasePO;

/**
 * @ClassName: 业务角色 
 * @author zy
 * @date 2018年7月24日 下午8:24:05
 */
@Entity
@Table(name="BVC_SYSTEM_BUSINESS_ROLE")
public class BusinessRolePO extends AbstractBasePO{

	private static final long serialVersionUID = 1L;
	
	/** 角色名称 */
	private String name;
	
	/** 特殊字段:比如主席，发言人，观众，自定义*/
	private BusinessRoleSpecial special;
	
	/** 角色业务类型：默认，可录制*/
	private BusinessRoleType type;

	private Set<RecordSchemePO> records;
	
	@Column(name = "NAME")
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	@Enumerated(value = EnumType.STRING)
	@Column(name = "SPECIAL")
	public BusinessRoleSpecial getSpecial() {
		return special;
	}

	public void setSpecial(BusinessRoleSpecial special) {
		this.special = special;
	}

	@Enumerated(value = EnumType.STRING)
	@Column(name = "TYPE")
	public BusinessRoleType getType() {
		return type;
	}

	public void setType(BusinessRoleType type) {
		this.type = type;
	}

	@OneToMany(mappedBy = "role", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = false)
	public Set<RecordSchemePO> getRecords() {
		return records;
	}

	public void setRecords(Set<RecordSchemePO> records) {
		this.records = records;
	}
	
}
