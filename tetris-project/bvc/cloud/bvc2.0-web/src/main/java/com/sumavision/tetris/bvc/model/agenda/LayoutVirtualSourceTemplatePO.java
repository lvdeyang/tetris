package com.sumavision.tetris.bvc.model.agenda;

import javax.jdo.annotations.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.sumavision.tetris.orm.po.AbstractBasePO;

/**
 * 议程布局下的分屏关联虚拟源<br/>
 * <p>详细描述</p>
 * <b>作者:</b>zsy<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2020年9月3日 上午10:01:11
 */
@Entity
@Table(name = "TETRIS_BVC_MODEL_LAYOUT_VIRTUAL_SOURCE_TEMPLATE")
public class LayoutVirtualSourceTemplatePO extends AbstractBasePO{

	private static final long serialVersionUID = 1L;
	
	/** 关联议程布局 AgendaLayoutTemplatePO */
	private Long agendaLayoutTemplateId;
	
	/** 分屏id */
	private Long layoutPositionId;
	
	/** 虚拟源id，实际就是合屏id */
	private Long virtualSourceId;

	public Long getAgendaLayoutTemplateId() {
		return agendaLayoutTemplateId;
	}

	public void setAgendaLayoutTemplateId(Long agendaLayoutTemplateId) {
		this.agendaLayoutTemplateId = agendaLayoutTemplateId;
	}

	public Long getLayoutPositionId() {
		return layoutPositionId;
	}

	public void setLayoutPositionId(Long layoutPositionId) {
		this.layoutPositionId = layoutPositionId;
	}

	public Long getVirtualSourceId() {
		return virtualSourceId;
	}

	public void setVirtualSourceId(Long virtualSourceId) {
		this.virtualSourceId = virtualSourceId;
	}
	
}
