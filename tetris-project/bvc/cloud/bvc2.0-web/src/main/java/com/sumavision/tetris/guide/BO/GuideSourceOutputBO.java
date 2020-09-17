package com.sumavision.tetris.guide.BO;

import java.util.List;

/**
 * 虚拟输出-输出源协议<br/>
 * <p>详细描述</p>
 * <b>作者:</b>lixin<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2020年9月8日 下午2:37:31
 */
public class GuideSourceOutputBO {

	
	/**绑定视频音频对应的 参数*/
	private List<GuideSourcesBO> sources;
	
	/**编码任务数组，按不同编码类型区分，分为视频编码，音频编码，字幕编码(可不考虑) */
	private List<GuideTaskArrayBO> task_array;
	
	private List<GuideOutputArrayBO> output_array;

	public List<GuideOutputArrayBO> getOutput_array() {
		return output_array;
	}

	public GuideSourceOutputBO setOutput_array(List<GuideOutputArrayBO> output_array) {
		this.output_array = output_array;
		return this;
	}

	public List<GuideSourcesBO> getSources() {
		return sources;
	}

	public GuideSourceOutputBO setSources(List<GuideSourcesBO> sources) {
		this.sources = sources;
		return this;
	}

	public List<GuideTaskArrayBO> getTask_array() {
		return task_array;
	}

	public GuideSourceOutputBO setTask_array(List<GuideTaskArrayBO> task_array) {
		this.task_array = task_array;
		return this;
	}
	
}
