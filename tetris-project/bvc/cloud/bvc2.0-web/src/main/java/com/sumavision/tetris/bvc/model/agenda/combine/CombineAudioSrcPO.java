package com.sumavision.tetris.bvc.model.agenda.combine;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import com.sumavision.tetris.orm.po.AbstractBasePO;

@Entity
@Table(name = "TETRIS_BVC_MODEL_AGENDA_COMBINE_AUDIO")
public class CombineAudioSrcPO extends AbstractBasePO{

	private static final long serialVersionUID = 1L;
	
	/** 源id */
	private Long srcId;
	
	/** 源类型 */
	private CombineAudioSrcType combineAudioSrcType;
	
	/** 隶属混音id */
	private Long combineAudioId;

	@Column(name = "SRC_ID")
	public Long getSrcId() {
		return srcId;
	}

	public void setSrcId(Long srcId) {
		this.srcId = srcId;
	}

	@Column(name = "COMBINE_AUDIO_SRC_TYPE")
	@Enumerated(value = EnumType.STRING)
	public CombineAudioSrcType getCombineAudioSrcType() {
		return combineAudioSrcType;
	}

	public void setCombineAudioSrcType(CombineAudioSrcType combineAudioSrcType) {
		this.combineAudioSrcType = combineAudioSrcType;
	}

	@Column(name = "COMBINE_AUDIO_ID")
	public Long getCombineAudioId() {
		return combineAudioId;
	}

	public void setCombineAudioId(Long combineAudioId) {
		this.combineAudioId = combineAudioId;
	}

}
