package com.sumavision.tetris.bvc.model.agenda.combine;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import com.sumavision.tetris.orm.po.AbstractBasePO;

/**
 * 议程混音<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2020年6月4日 下午3:33:01
 */
@Entity(name = "com.sumavision.tetris.bvc.model.agenda.combine.CombineAudioPO")
@Table(name = "TETRIS_BVC_MODEL_AGENDA_COMBINE_AUDIO")
public class CombineAudioPO extends AbstractBasePO{

	private static final long serialVersionUID = 1L;
	
	/** 名称 */
	private String name;
	
	/** 隶属业务id */
	private Long businessId;
	
	/** 隶属业务类型 */
	private CombineBusinessType businessType;
	
	/** 内容类型，用于自动合屏时标注 */
	private CombineContentType combineContentType;

	@Column(name = "NAME")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "BUSINESS_ID")
	public Long getBusinessId() {
		return businessId;
	}

	public void setBusinessId(Long businessId) {
		this.businessId = businessId;
	}

	@Column(name = "BUSINESS_TYPE")
	@Enumerated(value = EnumType.STRING)
	public CombineBusinessType getBusinessType() {
		return businessType;
	}

	public void setBusinessType(CombineBusinessType businessType) {
		this.businessType = businessType;
	}

	@Enumerated(value = EnumType.STRING)
	public CombineContentType getCombineContentType() {
		return combineContentType;
	}

	public void setCombineContentType(CombineContentType combineContentType) {
		this.combineContentType = combineContentType;
	}
	
}
