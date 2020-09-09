package com.sumavision.signal.bvc.director.bo;

import java.util.List;

/**
 * 添加任务数据<br/>
 * <b>作者:</b>wjw<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2020年1月21日 下午2:52:15
 */
public class AddTaskBO {

	private String taskId;
	
	private String select_index;
	
	private List<BundleBO> source;
	
	/** 先用string */
	private String transcodeParam;
	
	private List<BundleBO> destination;

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public List<BundleBO> getSource() {
		return source;
	}

	public void setSource(List<BundleBO> source) {
		this.source = source;
	}

	public String getTranscodeParam() {
		return transcodeParam;
	}

	public void setTranscodeParam(String transcodeParam) {
		this.transcodeParam = transcodeParam;
	}

	public List<BundleBO> getDestination() {
		return destination;
	}

	public void setDestination(List<BundleBO> destination) {
		this.destination = destination;
	}

	public String getSelect_index() {
		return select_index;
	}

	public void setSelect_index(String select_index) {
		this.select_index = select_index;
	}
	
}
