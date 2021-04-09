package com.sumavision.tetris.bvc.business.po.combine.video;

import javax.jdo.annotations.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.sumavision.tetris.orm.po.AbstractBasePO;

@Entity
@Table(name = "BVC_BUSINESS_COMBINE_TEMPLATE_GROUP_AGENDA_FORWARD_PERMISSION")
public class CombineTemplateGroupAgendeForwardPermissionPO extends AbstractBasePO{

	private static final long serialVersionUID = 1L;
	
	/** 关联group */
	private Long groupId;
	
	/** 关联合屏模板id */
	private Long combineTemplateId;
	
	/** 关联实际合屏（注意解绑） */
	private BusinessCombineVideoPO combineVideo;
	
	//---------以下是一些冗余的关联
	
	/** 针对的终端类型 */
	private Long terminalId;
	
	/** 议程id */
	private Long agendaId;
	
	/** 议程转发id */
	private Long agendaForwardId;
	
	/** 虚拟源（画面）id */
	private Long layoutId;
	
	//--------冗余关联结束

	@Column(name = "AGENDA_FORWARD_ID")
	public Long getAgendaForwardId() {
		return agendaForwardId;
	}

	public Long getGroupId() {
		return groupId;
	}

	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}

	public Long getTerminalId() {
		return terminalId;
	}

	public void setTerminalId(Long terminalId) {
		this.terminalId = terminalId;
	}

	public Long getAgendaId() {
		return agendaId;
	}

	public void setAgendaId(Long agendaId) {
		this.agendaId = agendaId;
	}

	public Long getCombineTemplateId() {
		return combineTemplateId;
	}

	public void setCombineTemplateId(Long combineTemplateId) {
		this.combineTemplateId = combineTemplateId;
	}

	@ManyToOne
	@JoinColumn(name = "COMBINE_VIDEO_ID")
	public BusinessCombineVideoPO getCombineVideo() {
		return combineVideo;
	}

	public void setCombineVideo(BusinessCombineVideoPO combineVideo) {
		this.combineVideo = combineVideo;
	}

	public void setAgendaForwardId(Long agendaForwardId) {
		this.agendaForwardId = agendaForwardId;
	}

	public Long getLayoutId() {
		return layoutId;
	}

	public void setLayoutId(Long layoutId) {
		this.layoutId = layoutId;
	}
	
}
