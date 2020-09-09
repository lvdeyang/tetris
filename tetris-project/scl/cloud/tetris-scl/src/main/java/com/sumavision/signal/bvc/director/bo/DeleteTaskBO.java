package com.sumavision.signal.bvc.director.bo;

import java.util.List;

/**
 * 删除任务数据<br/>
 * <b>作者:</b>wjw<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2020年2月13日 下午3:45:16
 */
public class DeleteTaskBO {
	
	/** 任务id */
	private String taskId;
	
	private List<BundleBO> source;

	public String getTaskId() {
		return taskId;
	}

	public DeleteTaskBO setTaskId(String taskId) {
		this.taskId = taskId;
		return this;
	}

	public List<BundleBO> getSource() {
		return source;
	}

	public DeleteTaskBO setSource(List<BundleBO> source) {
		this.source = source;
		return this;
	}
	
}
