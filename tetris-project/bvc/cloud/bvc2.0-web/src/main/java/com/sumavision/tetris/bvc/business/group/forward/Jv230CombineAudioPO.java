package com.sumavision.tetris.bvc.business.group.forward;

import javax.jdo.annotations.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.sumavision.tetris.orm.po.AbstractBasePO;

@Entity
@Table(name = "TETRIS_BVC_GROUP_JV230_COMBINE_AUDIO")
public class Jv230CombineAudioPO extends AbstractBasePO{

	private static final long serialVersionUID = 1L;
	
	/** 接入层id */
	private String layerId;

	@Column(name = "LAYER_ID")
	public String getLayerId() {
		return layerId;
	}

	public void setLayerId(String layerId) {
		this.layerId = layerId;
	}
	
}
