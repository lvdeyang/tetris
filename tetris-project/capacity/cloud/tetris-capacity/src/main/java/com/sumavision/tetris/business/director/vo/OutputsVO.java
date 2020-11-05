package com.sumavision.tetris.business.director.vo;

import java.util.List;

/**
 * 添加导播输出参数<br/>
 * <b>作者:</b>wjw<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2020年2月26日 上午9:13:45
 */
public class OutputsVO {

	private String taskId;
	
	private List<DestinationVO> dsts;

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public List<DestinationVO> getDsts() {
		return dsts;
	}

	public void setDsts(List<DestinationVO> dsts) {
		this.dsts = dsts;
	}
	
}
