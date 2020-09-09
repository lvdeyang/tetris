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
	
	/**虚拟源URL */
	private String output_udp_url;
	
	/**绑定视频音频对应的 参数*/
	private List<GuideSourcesBO> sources;
	
	/**编码任务数组，按不同编码类型区分，分为视频编码，音频编码，字幕编码(可不考虑) */
	private List<GuideTaskArrayBO> task_Array;

	public String getOutput_udp_url() {
		return output_udp_url;
	}

	public void setOutput_udp_url(String output_udp_url) {
		this.output_udp_url = output_udp_url;
	}

	public List<GuideSourcesBO> getSources() {
		return sources;
	}

	public void setSources(List<GuideSourcesBO> sources) {
		this.sources = sources;
	}

	public List<GuideTaskArrayBO> getTask_Array() {
		return task_Array;
	}

	public void setTask_Array(List<GuideTaskArrayBO> task_Array) {
		this.task_Array = task_Array;
	}
	
}
