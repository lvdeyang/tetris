package com.sumavision.tetris.bvc.model.agenda;

import javax.jdo.annotations.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.sumavision.tetris.orm.po.AbstractBasePO;

/**
 * 议程转发可选的虚拟源列表<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2020年10月23日 下午5:36:41
 */
@Entity
@Table(name = "TETRIS_BVC_MODEL_LAYOUT_SCOPE")
public class LayoutScopePO extends AbstractBasePO{

	private static final long serialVersionUID = 1L;

	/** 源数量 */
	private Integer sourceNumber;
	
	/** 虚拟源 */
	private Long layoutId;
	
	/** 议程转发id */
	private Long agendaForwardId;

	@Column(name = "SOURCE_NUMBER")
	public Integer getSourceNumber() {
		return sourceNumber;
	}

	public void setSourceNumber(Integer sourceNumber) {
		this.sourceNumber = sourceNumber;
	}

	@Column(name = "LAYOUT_ID")
	public Long getLayoutId() {
		return layoutId;
	}

	public void setLayoutId(Long layoutId) {
		this.layoutId = layoutId;
	}

	@Column(name = "AGENDA_FORWARD_ID")
	public Long getAgendaForwardId() {
		return agendaForwardId;
	}

	public void setAgendaForwardId(Long agendaForwardId) {
		this.agendaForwardId = agendaForwardId;
	}
	
}
