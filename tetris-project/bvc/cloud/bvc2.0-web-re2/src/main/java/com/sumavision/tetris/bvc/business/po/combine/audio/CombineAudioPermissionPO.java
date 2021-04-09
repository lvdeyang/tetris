package com.sumavision.tetris.bvc.business.po.combine.audio;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.sumavision.tetris.bvc.model.agenda.CustomAudioPermissionType;
import com.sumavision.tetris.orm.po.AbstractBasePO;

@Entity
@Table(name = "BVC_BUSINESS_COMBINE_AUDIO_PERMISSION")
public class CombineAudioPermissionPO extends AbstractBasePO{

	private static final long serialVersionUID = 1L;
	
	/** 关联group */
	private Long groupId;
	
	/** 关联类型：会议、议程、议程转发 */
	private CustomAudioPermissionType permissionType;
	
	/** 议程id/议程转发id/如果配置的是全局自动音频关联groupId */
	private Long permissionId;
	
	/** 议程id（当permissionType==AGENDA时，这个与permissionId相同） */
	private Long agendaId;
	
	/** 关联“全部的声音”（可能是混音/单音频） */
	private BusinessCombineAudioPO allAudio;
	
	//---------以下是一些冗余的关联
	
	/** 虚拟源id，仅在permissionType==AGENDA_FORWARD时有效 */
	private Long layoutId;
	
	//--------冗余关联结束

	public Long getGroupId() {
		return groupId;
	}

	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}

	@Enumerated(value = EnumType.STRING)
	public CustomAudioPermissionType getPermissionType() {
		return permissionType;
	}

	public void setPermissionType(CustomAudioPermissionType permissionType) {
		this.permissionType = permissionType;
	}

	public Long getPermissionId() {
		return permissionId;
	}

	public void setPermissionId(Long permissionId) {
		this.permissionId = permissionId;
	}

	public Long getAgendaId() {
		return agendaId;
	}

	public void setAgendaId(Long agendaId) {
		this.agendaId = agendaId;
	}

	@ManyToOne
	@JoinColumn(name = "ALL_AUDIO_ID")
	public BusinessCombineAudioPO getAllAudio() {
		return allAudio;
	}

	public void setAllAudio(BusinessCombineAudioPO allAudio) {
		this.allAudio = allAudio;
	}

	public Long getLayoutId() {
		return layoutId;
	}

	public void setLayoutId(Long layoutId) {
		this.layoutId = layoutId;
	}
	
}
