package com.sumavision.tetris.cs.channel.broad.ability.request;

import java.util.List;

public class BroadAbilityBroadRequestVO {
	/** 转换能力ip*/
	private String task_ip;
	
	/**转码模板*/
	private String template;
	
	/** 源具体信息 */
	private BroadAbilityBroadRequestInputVO input;
	
	/** 遍历时存储源信息，请求时不传 */
	private List<BroadAbilityBroadRequestInputPrevVO> map_sources;
	
	/** 编码设置*/
	private List<BroadAbilityRequestTaskVO> map_tasks;
	
	/** 输出信息 */
	private List<BroadAbilityBroadRequestOutputVO> map_outputs;
	
	/** 源媒体类型 */
	private String mediaType;
	
	
	public BroadAbilityBroadRequestInputVO getInput() {
		return input;
	}

	public BroadAbilityBroadRequestVO setInput(BroadAbilityBroadRequestInputVO input) {
		this.input = input;
		return this;
	}

	public String getTemplate() {
		return template;
	}

	public BroadAbilityBroadRequestVO setTemplate(String template) {
		this.template = template;
		return this;
	}

	public String getTask_ip() {
		return task_ip;
	}

	public BroadAbilityBroadRequestVO setTask_ip(String task_ip) {
		this.task_ip = task_ip;
		return this;
	}

	public List<BroadAbilityBroadRequestInputPrevVO> getMap_sources() {
		return map_sources;
	}

	public BroadAbilityBroadRequestVO setMap_sources(List<BroadAbilityBroadRequestInputPrevVO> map_sources) {
		this.map_sources = map_sources;
		return this;
	}

	public List<BroadAbilityBroadRequestOutputVO> getMap_outputs() {
		return map_outputs;
	}

	public BroadAbilityBroadRequestVO setMap_outputs(List<BroadAbilityBroadRequestOutputVO> map_outputs) {
		this.map_outputs = map_outputs;
		return this;
	}

	public List<BroadAbilityRequestTaskVO> getMap_tasks() {
		return map_tasks;
	}

	public BroadAbilityBroadRequestVO setMap_tasks(List<BroadAbilityRequestTaskVO> map_tasks) {
		this.map_tasks = map_tasks;
		return this;
	}

	public String getMediaType() {
		return mediaType;
	}

	public BroadAbilityBroadRequestVO setMediaType(String mediaType) {
		this.mediaType = mediaType;
		return this;
	}

}
