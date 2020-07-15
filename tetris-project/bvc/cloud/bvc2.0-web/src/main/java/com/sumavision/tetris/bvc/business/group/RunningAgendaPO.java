package com.sumavision.tetris.bvc.business.group;

import javax.persistence.Table;
import javax.persistence.Column;
import javax.persistence.Entity;

import com.sumavision.tetris.orm.po.AbstractBasePO;

/**
 * 业务组正在执行的议程<br/>
 * <b>作者:</b>zsy<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2020年6月23日 下午3:38:45
 */
@Entity
@Table(name = "TETRIS_BVC_BUSINESS_RUNNING_AGENDA")
public class RunningAgendaPO extends AbstractBasePO{

	private static final long serialVersionUID = 1L;
	
	/** 业务组id */
	private Long groupId;
	
	/** 议程id */
	private Long agendaId;
	
	/** 成员id */
	private Boolean running = true;

	@Column(name = "GROUP_ID")
	public Long getGroupId() {
		return groupId;
	}

	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}

	@Column(name = "AGENDA_ID")
	public Long getAgendaId() {
		return agendaId;
	}

	public void setAgendaId(Long agendaId) {
		this.agendaId = agendaId;
	}

	@Column(name = "RUNNING")
	public Boolean getRunning() {
		return running;
	}

	public void setRunning(Boolean running) {
		this.running = running;
	}

	public RunningAgendaPO(){}
	
	public RunningAgendaPO(Long groupId, Long agendaId){
		this.groupId = groupId;
		this.agendaId = agendaId;
	}
	
}
