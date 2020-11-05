package com.sumavision.signal.bvc.director.po;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.sumavision.tetris.orm.po.AbstractBasePO;

/**
 * 导播任务<br/>
 * <b>作者:</b>wjw<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2020年1月20日 下午2:52:57
 */
@Entity
@Table(name = "TETRIS_SCL_DIRECTOR_TASK")
public class DirectorTaskPO extends AbstractBasePO{

	private static final long serialVersionUID = 1L;
	
	/** 任务id */
	private String taskId;
	
	/** 选中索引 */
	private String select_index;
	
	/** 转码参数 */
	private String transcodeParam;

	@Column(name = "TASK_ID")
	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	@Column(name = "TRANSCODE_PARAM", columnDefinition = "longtext")
	public String getTranscodeParam() {
		return transcodeParam;
	}

	public void setTranscodeParam(String transcodeParam) {
		this.transcodeParam = transcodeParam;
	}

	@Column
	public String getSelect_index() {
		return select_index;
	}

	public void setSelect_index(String select_index) {
		this.select_index = select_index;
	}
}
