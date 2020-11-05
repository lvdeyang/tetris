package com.sumavision.tetris.guide.BO;

import java.util.List;

import com.sumavision.tetris.guide.control.SwitchingMode;

/**
 * 虚拟输出-输出源协议<br/>
 * <p>详细描述</p>
 * <b>作者:</b>lixin<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2020年9月8日 下午2:37:31
 */
public class GuideSourceOutputBO {
	
	/**绑定视频音频对应的 参数*/
	private List<GuideSourcesBO> map_sources;
	
	private String task_temple;

//	/**编码任务数组，按不同编码类型区分，分为视频编码，音频编码，字幕编码(可不考虑) */
//	private List<GuideTaskArrayBO> task_array;
	
	private List<GuideOutputArrayBO> map_outputs;
	
	private String task_common_type;
	
	private String task_common_mode="FRAME";
	
	public String getTask_temple() {
		return task_temple;
	}

	public GuideSourceOutputBO setTask_temple(String task_temple) {
		this.task_temple = task_temple;
		return this;
	}

	public List<GuideOutputArrayBO> getMap_outputs() {
		return map_outputs;
	}

	public GuideSourceOutputBO setMap_outputs(List<GuideOutputArrayBO> map_outputs) {
		this.map_outputs = map_outputs;
		return this;
	}

	public List<GuideSourcesBO> getMap_sources() {
		return map_sources;
	}

	public GuideSourceOutputBO setMap_sources(List<GuideSourcesBO> map_sources) {
		this.map_sources = map_sources;
		return this;
	}

//	public List<GuideTaskArrayBO> getTask_array() {
//		return task_array;
//	}
//
//	public GuideSourceOutputBO setTask_array(List<GuideTaskArrayBO> task_array) {
//		this.task_array = task_array;
//		return this;
//	}

	public String getTask_common_type() {
		return task_common_type;
	}

	public GuideSourceOutputBO setTask_common_type(String task_common_type) {
		this.task_common_type = task_common_type;
		return this;
	}

	public String getTask_common_mode() {
		return task_common_mode;
	}

	public GuideSourceOutputBO setTask_common_mode(String task_common_mode) {
		this.task_common_mode = task_common_mode;
		return this;
	}
	
	
}
