package com.sumavision.tetris.capacity.bo.input;

public class InputFileObjectBO {

	private String url;
	
	private Integer loop_count;

	public String getUrl() {
		return url;
	}

	public InputFileObjectBO setUrl(String url) {
		this.url = url;
		return this;
	}

	public Integer getLoop_count() {
		return loop_count;
	}

	public InputFileObjectBO setLoop_count(Integer loop_count) {
		this.loop_count = loop_count;
		return this;
	}
	
}
