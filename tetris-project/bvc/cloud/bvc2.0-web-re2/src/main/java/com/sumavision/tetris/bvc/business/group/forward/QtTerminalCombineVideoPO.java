package com.sumavision.tetris.bvc.business.group.forward;

import javax.jdo.annotations.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import com.sumavision.tetris.orm.po.AbstractBasePO;

/**
 * LQ项目： qt终端内容合屏<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2020年7月27日 下午4:35:04
 */
@Entity
@Table(name = "TETRIS_BVC_GROUP_QT_TERMINAL_COMBINE_VIDEO")
public class QtTerminalCombineVideoPO extends AbstractBasePO{

	private static final long serialVersionUID = 1L;

	/** 用户id */
	private String userId;
	
	/** 终端id */
	private Long terminalId;
	
	/** 业务 */
	private ForwardBusinessType businessType;

	@Column(name = "USER_ID")
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	@Column(name = "TERMINAL_ID")
	public Long getTerminalId() {
		return terminalId;
	}

	public void setTerminalId(Long terminalId) {
		this.terminalId = terminalId;
	}

	@Enumerated(value = EnumType.STRING)
	@Column(name = "BUSINESS_TYPE")
	public ForwardBusinessType getBusinessType() {
		return businessType;
	}

	public void setBusinessType(ForwardBusinessType businessType) {
		this.businessType = businessType;
	}
	
}
