package com.sumavision.tetris.cs.channel.broad.ability.request;

public class BroadAbilityBroadRequestInputPrevVO {
	/** 源类型 */
	private String type;
	
	/** 文件源参数 */
	private BroadAbilityBroadRequestInputPrevFileVO file;
	
	/** 流源参数 */
	private BroadAbilityBroadRequestInputPrevStreamVO stream;

	public String getType() {
		return type;
	}

	public BroadAbilityBroadRequestInputPrevVO setType(String type) {
		this.type = type;
		return this;
	}

	public BroadAbilityBroadRequestInputPrevFileVO getFile() {
		return file;
	}

	public BroadAbilityBroadRequestInputPrevVO setFile(BroadAbilityBroadRequestInputPrevFileVO file) {
		this.file = file;
		return this;
	}

	public BroadAbilityBroadRequestInputPrevStreamVO getStream() {
		return stream;
	}

	public BroadAbilityBroadRequestInputPrevVO setStream(BroadAbilityBroadRequestInputPrevStreamVO stream) {
		this.stream = stream;
		return this;
	}
}
