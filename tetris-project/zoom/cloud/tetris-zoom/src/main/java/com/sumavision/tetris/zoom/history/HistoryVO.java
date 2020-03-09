package com.sumavision.tetris.zoom.history;

import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.mvc.converter.AbstractBaseVO;

public class HistoryVO extends AbstractBaseVO<HistoryVO, HistoryPO>{

	/** 历史记录id */
	private Long id;
	
	/** 历史记录uuid */
	private String uuid;
	
	/** 历史记录创建时间 */
	private String updateTime;
	
	/** 历史对象id */
	private String historyId;
	
	/** 备注 */
	private String remark;

	/** 历史对象类型 */
	private String type;
	
	/** 用户id */
	private String userId;
	
	public Long getId() {
		return id;
	}

	public HistoryVO setId(Long id) {
		this.id = id;
		return this;
	}

	public String getUuid() {
		return uuid;
	}

	public HistoryVO setUuid(String uuid) {
		this.uuid = uuid;
		return this;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public HistoryVO setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
		return this;
	}

	public String getHistoryId() {
		return historyId;
	}

	public HistoryVO setHistoryId(String historyId) {
		this.historyId = historyId;
		return this;
	}

	public String getRemark() {
		return remark;
	}

	public HistoryVO setRemark(String remark) {
		this.remark = remark;
		return this;
	}

	public String getType() {
		return type;
	}

	public HistoryVO setType(String type) {
		this.type = type;
		return this;
	}

	public String getUserId() {
		return userId;
	}

	public HistoryVO setUserId(String userId) {
		this.userId = userId;
		return this;
	}

	@Override
	public HistoryVO set(HistoryPO entity) throws Exception {
		this.setId(entity.getId())
			.setUuid(entity.getUuid())
			.setUpdateTime(entity.getUpdateTime()==null?"":DateUtil.format(entity.getUpdateTime(), DateUtil.dateTimePattern))
			.setHistoryId(entity.getHistoryId())
			.setRemark(entity.getRemark())
			.setType(entity.getType().toString())
			.setUserId(entity.getUserId());
		return this;
	}
	
}
