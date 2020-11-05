package com.sumavision.bvc.command.system.po;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.sumavision.bvc.command.group.enumeration.ConfigType;
import com.sumavision.tetris.orm.po.AbstractBasePO;

/**
 * @ClassName: 系统级指挥配置（议程）
 * @author zsy
 * @date 2019年10月10日
 */
@Entity
@Table(name="BVC_COMMAND_SYSTEM_CONFIG")
public class CommandSystemConfigPO extends AbstractBasePO{
	
	private static final long serialVersionUID = 1L;
	
	/** 议程名称 */
	private String name;
	
	/** 配置类型 */
	private ConfigType type;//需要根据 ConfigType，给每一项都建一个PO
	
	/** 配置中的转发 */
	private Set<CommandSystemConfigForwardPO> forwards;

	@Column(name = "NAME")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@Enumerated(value = EnumType.STRING)
	@Column(name = "TYPE")
	public ConfigType getType() {
		return type;
	}

	public void setType(ConfigType type) {
		this.type = type;
	}

	@OneToMany(mappedBy = "config", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	public Set<CommandSystemConfigForwardPO> getForwards() {
		return forwards;
	}

	public void setForwards(Set<CommandSystemConfigForwardPO> forwards) {
		this.forwards = forwards;
	}
	
}
