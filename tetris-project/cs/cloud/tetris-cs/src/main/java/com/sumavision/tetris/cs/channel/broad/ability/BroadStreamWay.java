package com.sumavision.tetris.cs.channel.broad.ability;

public enum BroadStreamWay {
	ABILITY_STREAM("使用9000推流能力"),
	ABILITY_STREAM_TRANSCODE("云转码能力转码流"),
	ABILITY_FILE_STREAM_TRANSCODE("云转码能力推流转码");
	
	private String name;

	public String getName() {
		return name;
	}
	
	private BroadStreamWay(String name) {
		this.name = name;
	}
}
