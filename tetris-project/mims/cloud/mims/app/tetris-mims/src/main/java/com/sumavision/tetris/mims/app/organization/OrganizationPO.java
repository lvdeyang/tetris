package com.sumavision.tetris.mims.app.organization;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import com.sumavision.tetris.orm.po.AbstractBasePO;

@Entity
@Table(name = "MIMS_ORGANIZATION")
public class OrganizationPO extends AbstractBasePO{

	private static final long serialVersionUID = 1L;
	
	/** 角色名称 */
	private String name;
	
	/** 隶属组织 */
	private String groupId;
	
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

	@Column(name = "SERIAL")
	public int getSerial() {
		return serial;
	}

	public void setSerial(int serial) {
		this.serial = serial;
	}
}
