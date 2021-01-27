package com.sumavision.signal.bvc.entity.po.director;

import com.sumavision.signal.bvc.entity.enumeration.director.RateControlType;
import com.sumavision.signal.bvc.entity.enumeration.director.SwitchType;
import com.sumavision.tetris.orm.po.AbstractBasePO;

import javax.persistence.*;

/**
 * 转码任务
 */
@Entity
@Table(name = "BVC_DIRECTOR_TASK")
public class DirectorTaskPO extends AbstractBasePO{
	
	private static final long serialVersionUID = 1L;
	private String businessId;
	private SwitchType switchType;
	private RateControlType rateCtrl;
	private Integer bitRate;
	private String transcodeTemplate;

	public String getBusinessId() {
		return businessId;
	}

	@Enumerated(EnumType.STRING)
	public SwitchType getSwitchType() {
		return switchType;
	}

	public RateControlType getRateCtrl() {
		return rateCtrl;
	}

	public Integer getBitRate() {
		return bitRate;
	}

	public String getTranscodeTemplate() {
		return transcodeTemplate;
	}

	public void setBusinessId(String businessId) {
		this.businessId = businessId;
	}
	@Enumerated(EnumType.STRING)
	public void setSwitchType(SwitchType switchType) {
		this.switchType = switchType;
	}

	public void setRateCtrl(RateControlType rateCtrl) {
		this.rateCtrl = rateCtrl;
	}

	public void setBitRate(Integer bitRate) {
		this.bitRate = bitRate;
	}

	public void setTranscodeTemplate(String transcodeTemplate) {
		this.transcodeTemplate = transcodeTemplate;
	}
}
