package com.sumavision.tetris.business.director.vo;

import java.util.List;

/**
 * 导播任务参数<br/>
 * <b>作者:</b>wjw<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2020年2月14日 下午2:43:58
 */
public class DirectorTaskVO {

	/** 任务id */
	private String taskId;
	
	/** 能力ip */
	private String capacityIp;
	
	/** 选中索引源 */
	private String select_index;
	
	/** 输入源 */
	private List<SourceVO> sources;
	
	/** 转码参数 */
	private TranscodeVO transcode;
	
	/** 输出 */
	private List<DestinationVO> destinations;

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getCapacityIp() {
		return capacityIp;
	}

	public void setCapacityIp(String capacityIp) {
		this.capacityIp = capacityIp;
	}
	
	public List<SourceVO> getSources() {
		return sources;
	}

	public void setSources(List<SourceVO> sources) {
		this.sources = sources;
	}

	public String getSelect_index() {
		return select_index;
	}

	public void setSelect_index(String select_index) {
		this.select_index = select_index;
	}

	public TranscodeVO getTranscode() {
		return transcode;
	}

	public void setTranscode(TranscodeVO transcode) {
		this.transcode = transcode;
	}

	public List<DestinationVO> getDestinations() {
		return destinations;
	}

	public void setDestinations(List<DestinationVO> destinations) {
		this.destinations = destinations;
	}
	
}
