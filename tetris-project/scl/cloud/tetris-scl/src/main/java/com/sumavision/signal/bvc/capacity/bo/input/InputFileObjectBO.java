package com.sumavision.signal.bvc.capacity.bo.input;

public class InputFileObjectBO {

	private String url;
	
	private Integer loop_count;
	
	private Integer start_ms = 0;
	
	private Integer duration;

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

	public Integer getStart_ms() {
		return start_ms;
	}

	public InputFileObjectBO setStart_ms(Integer start_ms) {
		this.start_ms = start_ms;
		return this;
	}

	public Integer getDuration() {
		return duration;
	}

	public InputFileObjectBO setDuration(Integer duration) {
		this.duration = duration;
		return this;
	}
	
}
