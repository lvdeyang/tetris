package com.sumavision.tetris.bvc.model.agenda;

import javax.jdo.annotations.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import com.sumavision.tetris.orm.po.AbstractBasePO;

/**
 * 议程转发源<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2020年10月23日 下午5:41:26
 */
@Entity
@Table(name = "TETRIS_BVC_MODEL_AGENDA_FORWARD_SOURCE")
public class AgendaForwardSourcePO extends AbstractBasePO{

	private static final long serialVersionUID = 1L;

	/** 源id,角色通道id、成员音频通道id*/
	private Long sourceId;
	
	/** 源类型，目前支持角色通道和会场通道 */
	private SourceType sourceType;
	
	/** 源对应的虚拟源布局序号 */
	private Integer serialNum;
	
	/** 源与虚拟源布局序号对应方式，如果取值CONFIRM，则读serialNum值，如果取值AUTO_INCREMENT，则根据转发中选择的虚拟源布局进行自增 */
	private LayoutPositionSelectionType layoutPositionSelectionType;
	
	/** 是否轮询，只有当layoutPositionSelectionType取值为CONFIRM时该值才有可能取值为true */
	private Boolean isLoop;
	
	/** 如果轮询记录轮询间隔，单位：秒 */
	private Integer loopTime;
	
	/** 议程转发id */
	private Long agendaForwardId;

	@Column(name = "SOURCE_ID")
	public Long getSourceId() {
		return sourceId;
	}

	public void setSourceId(Long sourceId) {
		this.sourceId = sourceId;
	}

	@Enumerated(value  = EnumType.STRING)
	@Column(name = "SOURCE_TYPE")
	public SourceType getSourceType() {
		return sourceType;
	}

	public void setSourceType(SourceType sourceType) {
		this.sourceType = sourceType;
	}

	@Column(name = "SERIAL_NUM")
	public Integer getSerialNum() {
		return serialNum;
	}

	public void setSerialNum(Integer serialNum) {
		this.serialNum = serialNum;
	}

	@Enumerated(value = EnumType.STRING)
	@Column(name = "LAYOUT_POSITION_SELECTION_TYPE")
	public LayoutPositionSelectionType getLayoutPositionSelectionType() {
		return layoutPositionSelectionType;
	}

	public void setLayoutPositionSelectionType(LayoutPositionSelectionType layoutPositionSelectionType) {
		this.layoutPositionSelectionType = layoutPositionSelectionType;
	}

	@Column(name = "IS_LOOP")
	public Boolean getIsLoop() {
		return isLoop;
	}

	public void setIsLoop(Boolean isLoop) {
		this.isLoop = isLoop;
	}

	@Column(name = "LOOP_TIME")
	public Integer getLoopTime() {
		return loopTime;
	}

	public void setLoopTime(Integer loopTime) {
		this.loopTime = loopTime;
	}

	@Column(name = "AGENDA_FORWARD_ID")
	public Long getAgendaForwardId() {
		return agendaForwardId;
	}

	public void setAgendaForwardId(Long agendaForwardId) {
		this.agendaForwardId = agendaForwardId;
	}
	
}
