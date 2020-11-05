package com.sumavision.tetris.record.strategy;

import java.util.Date;

public class RecordStrategyTimerBO {
	private Long id;

	private String name;

	private Date operateTime;

	private Long strategyId;

	private Long duration;

	private String recordResult;

	private boolean isStart;

	private boolean isCut;

	public String getName() {
		return name;
	}

	public Long getStrategyId() {
		return strategyId;
	}

	public Long getDuration() {
		return duration;
	}

	public String getRecordResult() {
		return recordResult;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setStrategyId(Long strategyId) {
		this.strategyId = strategyId;
	}

	public void setDuration(Long duration) {
		this.duration = duration;
	}

	public void setRecordResult(String recordResult) {
		this.recordResult = recordResult;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public boolean isStart() {
		return isStart;
	}

	public void setStart(boolean isStart) {
		this.isStart = isStart;
	}

	public Date getOperateTime() {
		return operateTime;
	}

	public void setOperateTime(Date operateTime) {
		this.operateTime = operateTime;
	}

	public boolean isCut() {
		return isCut;
	}

	public void setCut(boolean isCut) {
		this.isCut = isCut;
	}

}
