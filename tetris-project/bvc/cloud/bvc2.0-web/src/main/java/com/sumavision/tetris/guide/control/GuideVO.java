/**
 * 
 */
package com.sumavision.tetris.guide.control;

import java.util.Date;

import com.sumavision.tetris.mvc.converter.AbstractBaseVO;

/**
 * 类型概述<br/>
 * <p>详细描述</p>
 * <b>作者:</b>Administrator<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2020年9月3日 下午4:55:35
 */
public class GuideVO extends AbstractBaseVO<GuideVO, GuidePO>{

	/** 导播任务编号 */
	private Long index;

	/** 名称 */
	private String taskName;

	/** 直播时长 */
	private String liveBroadcastDuration;

	/** 创建时间 */
	private Date creationTime;

	/** 视频参数编号 */
	private Long parameterNumber;
	
	public Long getIndex() {
		return index;
	}

	public GuideVO setIndex(Long index) {
		this.index = index;
		return this;
	}

	public String getTaskName() {
		return taskName;
	}

	public GuideVO setTaskName(String taskName) {
		this.taskName = taskName;
		return this;
	}

	public String getLiveBroadcastDuration() {
		return liveBroadcastDuration;
	}

	public GuideVO setLiveBroadcastDuration(String liveBroadcastDuration) {
		this.liveBroadcastDuration = liveBroadcastDuration;
		return this;
	}

	public Date getCreationTime() {
		return creationTime;
	}

	public GuideVO setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
		return this;
	}

	public Long getParameterNumber() {
		return parameterNumber;
	}

	public GuideVO setParameterNumber(Long parameterNumber) {
		this.parameterNumber = parameterNumber;
		return this;
	}

	@Override
	public GuideVO set(GuidePO entity) throws Exception {
		this.setIndex(entity.getIndex())
			.setTaskName(entity.getTaskName())
			.setLiveBroadcastDuration(entity.getLiveBroadcastDuration())
			.setCreationTime(entity.getCreationTime());
			
		return this;
	}

}
