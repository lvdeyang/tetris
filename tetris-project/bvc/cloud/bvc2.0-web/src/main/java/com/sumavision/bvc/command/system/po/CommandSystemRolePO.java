package com.sumavision.bvc.command.system.po;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.sumavision.bvc.command.group.enumeration.CommandRoleSpecial;
import com.sumavision.bvc.command.group.enumeration.ForwardMode;
import com.sumavision.bvc.system.enumeration.BusinessRoleSpecial;
import com.sumavision.tetris.orm.po.AbstractBasePO;

/**
 * @ClassName: 系统级的指挥角色
 * @author zsy
 * @date 2019年10月10日
 */
@Entity
@Table(name="BVC_COMMAND_SYSTEM_ROLE")
public class CommandSystemRolePO extends AbstractBasePO{

	private static final long serialVersionUID = 1L;
	
	/** 角色名称 */
	private String name;
	
	/** 特殊字段:比如主席，发言人，观众，自定义*/
	private CommandRoleSpecial special;
	
//	/** 角色业务类型：默认，可录制*/
//	private BusinessRoleType type;
	
	@Column(name = "NAME")
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	@Enumerated(value = EnumType.STRING)
	@Column(name = "SPECIAL")
	public CommandRoleSpecial getSpecial() {
		return special;
	}
	
	public void setSpecial(CommandRoleSpecial special) {
		this.special = special;
	}
	
//	@Enumerated(value = EnumType.STRING)
//	@Column(name = "TYPE")
//	public BusinessRoleType getType() {
//		return type;
//	}
//
//	public void setType(BusinessRoleType type) {
//		this.type = type;
//	}
	
}
