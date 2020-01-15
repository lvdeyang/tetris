package com.sumavision.bvc.system.po;

import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.sumavision.tetris.orm.po.AbstractBasePO;

/**
 * @ClassName: 会议模板<br/> 
 * @Description: 一个会议模板包含：一个录制方案，多个角色、多个布局方案、多个通道别名<br/>
 * @author lvdeyang
 * @date 2018年7月24日 下午3:58:09 
 */
@Entity
@Table(name="BVC_SYSTEM_TPL")
public class TplPO extends AbstractBasePO{
	
	private static final long serialVersionUID = 1L;
	
	/** 模板名称 */
	private String name;
	
	/** 自动建立的议程id，逗号分隔 */
	private String autoBuildAgendaIds;
	
	private Set<TplContentPO> contents;
	
	@Column(name = "NAME")
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	@OneToMany(mappedBy = "tpl", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	public Set<TplContentPO> getContents() {
		return contents;
	}

	public void setContents(Set<TplContentPO> contents) {
		this.contents = contents;
	}

	public String getAutoBuildAgendaIds() {
		return autoBuildAgendaIds;
	}

	public void setAutoBuildAgendaIds(String autoBuildAgendaIds) {
		this.autoBuildAgendaIds = autoBuildAgendaIds;
	}
	
}
