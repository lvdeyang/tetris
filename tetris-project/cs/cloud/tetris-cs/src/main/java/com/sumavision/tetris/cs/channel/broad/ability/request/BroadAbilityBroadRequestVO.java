package com.sumavision.tetris.cs.channel.broad.ability.request;

import java.util.List;

public class BroadAbilityBroadRequestVO {
	/** 转换能力ip */
	private String deviceIp;
	
	/** 源媒体类型 */
	private String mediaType;
	
	/** 源具体信息 */
	private BroadAbilityBroadRequestInputVO input;
	
	/** 遍历时存储源信息，请求时不传 */
	private List<BroadAbilityBroadRequestInputPrevVO> sources;
	
	/** 输出信息 */
	private List<BroadAbilityBroadRequestOutputVO> output;

	public String getDeviceIp() {
		return deviceIp;
	}

	public BroadAbilityBroadRequestVO setDeviceIp(String deviceIp) {
		this.deviceIp = deviceIp;
		return this;
	}

	public String getMediaType() {
		return mediaType;
	}

	public BroadAbilityBroadRequestVO setMediaType(String mediaType) {
		this.mediaType = mediaType;
		return this;
	}

	public BroadAbilityBroadRequestInputVO getInput() {
		return input;
	}

	public BroadAbilityBroadRequestVO setInput(BroadAbilityBroadRequestInputVO input) {
		this.input = input;
		return this;
	}

	public List<BroadAbilityBroadRequestInputPrevVO> getSources() {
		return sources;
	}

	public BroadAbilityBroadRequestVO setSources(List<BroadAbilityBroadRequestInputPrevVO> sources) {
		this.sources = sources;
		return this;
	}

	public List<BroadAbilityBroadRequestOutputVO> getOutput() {
		return output;
	}

	public BroadAbilityBroadRequestVO setOutput(List<BroadAbilityBroadRequestOutputVO> output) {
		this.output = output;
		return this;
	}
}
