package com.sumavision.bvc.basic.po;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import com.sumavision.bvc.basic.enumeration.RoleSpecial;
import com.sumavision.bvc.command.group.enumeration.CommandRoleSpecial;
import com.sumavision.tetris.orm.po.AbstractBasePO;

/**
 * 通用角色<br/>
 * <b>作者:</b>wjw<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2019年10月12日 下午5:23:24
 */
@Entity
@Table(name="BVC_SYSTEM_ROLE")
public class BasicRolePO extends AbstractBasePO{

	private static final long serialVersionUID = 1L;
	
	/** 角色名称 */
	private String name;
	
	/** 角色功能:发言人，观众*/
	private RoleSpecial special;
	
	/** 是否是虚拟设备 */
	private boolean isVirtualDevice;
	
	@Column(name = "NAME")
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	@Enumerated(value = EnumType.STRING)
	@Column(name = "SPECIAL")
	public RoleSpecial getSpecial() {
		return special;
	}
	
	public void setSpecial(RoleSpecial special) {
		this.special = special;
	}

	@Column(name = "IS_VIRTUAL_DEVICE")
	public boolean isVirtualDevice() {
		return isVirtualDevice;
	}

	public void setVirtualDevice(boolean isVirtualDevice) {
		this.isVirtualDevice = isVirtualDevice;
	}
	
}
