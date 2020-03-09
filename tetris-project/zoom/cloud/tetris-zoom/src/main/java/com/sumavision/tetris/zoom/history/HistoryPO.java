package com.sumavision.tetris.zoom.history;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import com.sumavision.tetris.orm.po.AbstractBasePO;

@Entity
@Table(name = "TETRIS_HISTORY")
public class HistoryPO extends AbstractBasePO{

	private static final long serialVersionUID = 1L;
	
	/** 历史对象id */
	private String historyId;
	
	/** 备注 */
	private String remark;

	/** 历史对象类型 */
	private HistoryType type;
	
	/** 用户id */
	private String userId;

	@Column(name = "HISTORY_ID")
	public String getHistoryId() {
		return historyId;
	}

	public void setHistoryId(String historyId) {
		this.historyId = historyId;
	}

	@Column(name = "REMARK")
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Enumerated(value = EnumType.STRING)
	@Column(name = "TYPE")
	public HistoryType getType() {
		return type;
	}

	public void setType(HistoryType type) {
		this.type = type;
	}

	@Column(name = "USER_ID")
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
	
}
