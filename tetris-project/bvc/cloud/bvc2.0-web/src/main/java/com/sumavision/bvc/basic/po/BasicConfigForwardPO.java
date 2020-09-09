package com.sumavision.bvc.basic.po;

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
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.sumavision.bvc.basic.enumeration.ConfigForwardType;
import com.sumavision.tetris.orm.po.AbstractBasePO;

/**
 * 通用议程转发<br/>
 * <b>作者:</b>wjw<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2019年10月12日 下午5:21:10
 */
@Entity
@Table(name="BVC_BASICCONFIG_FORWARD")
public class BasicConfigForwardPO extends AbstractBasePO{
	
	private static final long serialVersionUID = 1L;
	
	/** 转发类型 */
	private ConfigForwardType type;
	
	/** 转发源角色 */
	private BasicConfigForwardSrcRolePO src;
	
	/** 转发目的角色 */
	private Set<BasicConfigForwardDstRolePO> dsts;
	
	/** 关联配置 */
	private BasicConfigPO config;

	@Enumerated(value = EnumType.STRING)
	@Column(name = "TYPE")
	public ConfigForwardType getType() {
		return type;
	}

	public void setType(ConfigForwardType type) {
		this.type = type;
	}

	@OneToOne(mappedBy = "forward", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
	public BasicConfigForwardSrcRolePO getSrc() {
		return src;
	}

	public void setSrc(BasicConfigForwardSrcRolePO src) {
		this.src = src;
	}

	@OneToMany(mappedBy = "forward", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	public Set<BasicConfigForwardDstRolePO> getDsts() {
		return dsts;
	}

	public void setDsts(Set<BasicConfigForwardDstRolePO> dsts) {
		this.dsts = dsts;
	}

	@ManyToOne
	@JoinColumn(name = "CONFIG_ID")
	public BasicConfigPO getConfig() {
		return config;
	}

	public void setConfig(BasicConfigPO config) {
		this.config = config;
	}

	
}
