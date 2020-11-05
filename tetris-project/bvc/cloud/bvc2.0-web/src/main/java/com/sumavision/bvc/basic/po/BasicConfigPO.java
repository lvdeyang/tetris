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
import javax.persistence.Table;

import com.sumavision.bvc.command.group.enumeration.ConfigType;
import com.sumavision.tetris.orm.po.AbstractBasePO;

/**
 * 通用议程<br/>
 * <b>作者:</b>wjw<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2019年10月12日 下午5:22:11
 */
@Entity
@Table(name="BVC_BASIC_CONFIG")
public class BasicConfigPO extends AbstractBasePO{
	
	private static final long serialVersionUID = 1L;
	
	/** 议程名称 */
	private String name;
	
	/** 议程标签，业务用来判断议程用途 */
	private String tag;
	
	/** 配置中的转发 */
	private Set<BasicConfigForwardPO> forwards;

	@Column(name = "NAME")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "TAG")
	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	@OneToMany(mappedBy = "config", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	public Set<BasicConfigForwardPO> getForwards() {
		return forwards;
	}

	public void setForwards(Set<BasicConfigForwardPO> forwards) {
		this.forwards = forwards;
	}
	
}
